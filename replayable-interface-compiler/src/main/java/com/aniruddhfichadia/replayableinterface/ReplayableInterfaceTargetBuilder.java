package com.aniruddhfichadia.replayableinterface;


import com.aniruddhfichadia.replayableinterface.ReplayableInterface.ReplayType;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.util.List;
import java.util.Set;

import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.Name;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.ElementFilter;

import static com.aniruddhfichadia.replayableinterface.DelegatorBuilder.FIELD_NAME_DELEGATE;
import static com.aniruddhfichadia.replayableinterface.ReplaySourceBuilder.FIELD_NAME_ACTIONS;
import static com.aniruddhfichadia.replayableinterface.ReplaySourceBuilder.PARAM_NAME_TARGET;
import static com.aniruddhfichadia.replayableinterface.ReplayableInterfaceProcessor.REPLAYABLE_ACTION;


/**
 * @author Aniruddh Fichadia
 * @date 21/1/17
 */
public class ReplayableInterfaceTargetBuilder {
    public static final ClassName UUID = ClassName.get("java.util", "UUID");

    private static final String VAR_NAME_ACTION_KEY = "_actionKey";


    private final TypeSpec.Builder classBuilder;
    private final Element          baseElement;
    private final ClassName        targetClassName;
    private final ReplayType       replayType;
    private final ReplayStrategy   defaultReplyStrategy;


    public ReplayableInterfaceTargetBuilder(TypeSpec.Builder classBuilder, ClassName targetClassName,
                                            Element baseElement, ReplayType replayType,
                                            ReplayStrategy defaultReplyStrategy) {
        super();

        this.classBuilder = classBuilder;
        this.targetClassName = targetClassName;
        this.baseElement = baseElement;
        this.replayType = replayType;
        this.defaultReplyStrategy = defaultReplyStrategy;
    }

    public ReplayableInterfaceTargetBuilder applyClassDefinition() {
        classBuilder.addSuperinterface(targetClassName);
        return this;
    }


    public ReplayableInterfaceTargetBuilder applyMethods() {
        List<ExecutableElement> methods = ElementFilter.methodsIn(
                baseElement.getEnclosedElements());

        for (ExecutableElement method : methods) {
            String methodName = method.getSimpleName().toString();
            List<? extends VariableElement> methodParameters = method.getParameters();

            ReplayableMethod methodAnnotation = method.getAnnotation(ReplayableMethod.class);
            ReplayStrategy replayStrategy = resolveStrategy(methodAnnotation);
            String group = methodAnnotation == null ? null : methodAnnotation.group();

            classBuilder.addMethod(createImplementedMethod(methodName, methodParameters,
                                                           replayStrategy, group));
        }

        return this;
    }


    private ReplayStrategy resolveStrategy(ReplayableMethod methodAnnotation) {
        if (methodAnnotation != null) {
            ReplayStrategy methodReplayStrategy = methodAnnotation.value();

            return methodReplayStrategy != ReplayStrategy.DEFAULT
                   ? methodReplayStrategy
                   : ReplayStrategy.ENQUEUE_LAST_ONLY;
        } else {
            return defaultReplyStrategy;
        }
    }


    private MethodSpec createImplementedMethod(String name, List<? extends VariableElement> parameters,
                                               ReplayStrategy replayStrategy, String group) {
        MethodSpec.Builder methodBuilder =
                MethodSpec.methodBuilder(name)
                          .addAnnotation(Override.class)
                          .addModifiers(Modifier.PUBLIC)
                          .addJavadoc("@ReplayStrategy " + replayStrategy + "\n");

        StringBuilder allParamNamesBuilder = new StringBuilder();
        StringBuilder allParamTypesBuilder = new StringBuilder();
        StringBuilder castParamsFromParamsBuilder = new StringBuilder();
        for (int i = 0; i < parameters.size(); i++) {
            VariableElement parameter = parameters.get(i);

            TypeName paramType = TypeName.get(parameter.asType());
            Name paramName = parameter.getSimpleName();

            Set<Modifier> modifiers = parameter.getModifiers();
            Modifier[] modifierArr = new Modifier[modifiers.size()];
            modifiers.toArray(modifierArr);

            methodBuilder.addParameter(paramType,
                                       paramName.toString(),
                                       modifierArr);

            allParamNamesBuilder.append(paramName);
            allParamTypesBuilder.append(paramType);
            // TODO: pretty this
            castParamsFromParamsBuilder.append("(")
                                       .append(paramType)
                                       .append(") params[")
                                       .append(i)
                                       .append("]");

            if (i < parameters.size() - 1) {
                final String separator = ", ";

                allParamNamesBuilder.append(separator);
                allParamTypesBuilder.append(separator);
                castParamsFromParamsBuilder.append(separator);
            }
        }

        String allParamNames = allParamNamesBuilder.toString();
        String allParamTypes = allParamTypesBuilder.toString();
        String castParamsFromParams = castParamsFromParamsBuilder.toString();

        CodeBlock.Builder methodCode = CodeBlock.builder();
        methodCode.beginControlFlow("if (this.$L != null)", FIELD_NAME_DELEGATE)
                  .addStatement("this.$L.$L($L)", FIELD_NAME_DELEGATE, name, allParamNames);

        if (ReplayType.DELEGATE_AND_REPLAY == replayType) {
            methodCode.endControlFlow();
        }

        if (!ReplayStrategy.NONE.equals(replayStrategy)) {
            if (ReplayType.REPLAY_IF_NO_DELEGATE == replayType) {
                methodCode.nextControlFlow("else");
            }

            methodCode.add(createActionKey(name, parameters, allParamTypes, group, replayStrategy))
                      .add("");
            methodCode.addStatement("this.$L.remove($L)", FIELD_NAME_ACTIONS, VAR_NAME_ACTION_KEY);
            methodCode.addStatement("this.$L.put($L, $L)", FIELD_NAME_ACTIONS,
                                    VAR_NAME_ACTION_KEY, createAnonymousReplayableAction(name,
                                                                                         allParamNames,
                                                                                         castParamsFromParams));
        }

        if (ReplayType.REPLAY_IF_NO_DELEGATE == replayType) {
            methodCode.endControlFlow();
        }

        methodBuilder.addCode(methodCode.build());

        return methodBuilder.build();
    }

    private CodeBlock createActionKey(String methodName, List<? extends VariableElement> parameters,
                                      String allParamTypes, String group,
                                      ReplayStrategy replayStrategy) {
        CodeBlock.Builder actionKeyCode = CodeBlock.builder()
                                                   .add("String $L = ", VAR_NAME_ACTION_KEY);
        switch (replayStrategy) {
            case ENQUEUE:
                actionKeyCode.addStatement("$T.randomUUID().toString()", UUID);
                break;
            case ENQUEUE_LAST_ONLY:
                actionKeyCode.addStatement("\"$L($L)\"", methodName, allParamTypes);
                break;
            case ENQUEUE_LAST_IN_GROUP:
                actionKeyCode.addStatement("\"group: $L\"", group);
                break;
            case ENQUEUE_PARAM_UNIQUE:
                actionKeyCode.add("\"$L(", methodName);

                for (int i = 0; i < parameters.size(); i++) {
                    VariableElement parameter = parameters.get(i);

                    TypeName paramType = TypeName.get(parameter.asType());
                    Name paramName = parameter.getSimpleName();

                    actionKeyCode.add("$L: \" + ", paramType.toString());

                    if (paramType.isPrimitive()) {
                        actionKeyCode.add(paramName.toString());
                    } else {
                        actionKeyCode.add("($L == null ? \"null\" : $L.hashCode())", paramName,
                                          paramName);
                    }

                    if (i < parameters.size() - 1) {
                        actionKeyCode.add(" + \", ");
                    }
                }
                actionKeyCode.add(" + \")\"");
                actionKeyCode.add(";\n");
                break;
            default:
                throw new IllegalArgumentException("Unsupported ReplayStrategy " + replayStrategy);
        }

        return actionKeyCode.build();
    }


    private TypeSpec createAnonymousReplayableAction(String methodName, String allParamNames,
                                                     String castParamsFromParams) {
        return TypeSpec.anonymousClassBuilder(allParamNames)
                       .superclass(ParameterizedTypeName.get(REPLAYABLE_ACTION, targetClassName))
                       .addMethod(MethodSpec.methodBuilder("replayOnTarget")
                                            .addAnnotation(Override.class)
                                            .addModifiers(Modifier.PUBLIC)
                                            .addParameter(targetClassName, PARAM_NAME_TARGET)
                                            .addCode(CodeBlock.builder()
                                                              .addStatement("$L.$L($L)",
                                                                            PARAM_NAME_TARGET,
                                                                            methodName,
                                                                            castParamsFromParams)
                                                              .build())
                                            .build())
                       .build();
    }
}