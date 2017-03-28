package com.aniruddhfichadia.replayableinterface;


import com.aniruddhfichadia.replayableinterface.ReplayableInterface.ReplayType;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.Name;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.ElementFilter;

import static com.aniruddhfichadia.replayableinterface.DelegatorVisitor.FIELD_NAME_DELEGATE_REFERENCE;
import static com.aniruddhfichadia.replayableinterface.DelegatorVisitor.METHOD_NAME_IS_DELEGATE_BOUND;
import static com.aniruddhfichadia.replayableinterface.ReplaySourceVisitor.METHOD_NAME_ADD_REPLAYABLE_ACTION;
import static com.aniruddhfichadia.replayableinterface.ReplayableActionBuilder.FIELD_NAME_PARAMS;
import static com.aniruddhfichadia.replayableinterface.ReplayableInterfaceProcessor.REPLAY_STRATEGY;


/**
 * @author Aniruddh Fichadia
 * @date 21/1/17
 */
public class ReplayableInterfaceTargetVisitor {
    public static final ClassName UUID                   = ClassName.get("java.util", "UUID");
    public static final ClassName NULL_POINTER_EXCEPTION = ClassName.get("java.lang",
                                                                         "NullPointerException");

    private static final String VAR_NAME_ACTION_KEY = "_actionKey";


    private final TypeSpec.Builder classBuilder;
    private final Element          baseElement;
    private final ClassName        targetClassName;
    private final ReplayType       replayType;
    private final ReplayStrategy   defaultReplyStrategy;


    private final List<String> warnings;
    private final List<String> errors;


    public ReplayableInterfaceTargetVisitor(TypeSpec.Builder classBuilder, ClassName targetClassName,
                                            Element baseElement, ReplayType replayType,
                                            ReplayStrategy defaultReplyStrategy) {
        super();

        this.classBuilder = classBuilder;
        this.targetClassName = targetClassName;
        this.baseElement = baseElement;
        this.replayType = replayType;
        this.defaultReplyStrategy = defaultReplyStrategy;
        this.warnings = new ArrayList<>();
        this.errors = new ArrayList<>();
    }

    public ReplayableInterfaceTargetVisitor applyClassDefinition() {
        classBuilder.addSuperinterface(targetClassName);
        return this;
    }


    public ReplayableInterfaceTargetVisitor applyMethods() {
        List<ExecutableElement> methods = ElementFilter.methodsIn(
                baseElement.getEnclosedElements());

        for (ExecutableElement method : methods) {
            classBuilder.addMethod(createImplementedMethod(method));
        }

        return this;
    }


    private MethodSpec createImplementedMethod(ExecutableElement method) {
        String methodName = method.getSimpleName().toString();
        List<? extends VariableElement> methodParameters = method.getParameters();
        TypeMirror methodReturnType = method.getReturnType();
        boolean methodReturnsNonVoidValue = methodReturnType.getKind() != TypeKind.VOID;

        ReplayableMethod methodAnnotation = method.getAnnotation(ReplayableMethod.class);
        ReplayStrategy replayStrategy = resolveStrategy(methodAnnotation);
        String group = methodAnnotation == null ? null : methodAnnotation.group();

        if (methodReturnsNonVoidValue) {
            // TODO probably should not get to this point at all
            warnings.add(String.format(
                    "%s returns a value, this will throw a NullPointerException when the delegate is not bound",
                    methodName));
        }

        MethodSpec.Builder methodBuilder =
                MethodSpec.methodBuilder(methodName)
                          .addAnnotation(Override.class)
                          .addModifiers(Modifier.PUBLIC)
                          .returns(TypeName.get(methodReturnType))
                          .addJavadoc("Built using {@link $T#" + replayStrategy + "}\n",
                                      REPLAY_STRATEGY);

        StringBuilder allParamNamesBuilder = new StringBuilder();
        StringBuilder allParamTypesBuilder = new StringBuilder();
        CodeBlock.Builder replayOnTargetBuilder =
                CodeBlock.builder()
                         .add("$L.$L(", ReplayableActionBuilder.PARAM_NAME_TARGET, methodName);

        for (int i = 0; i < methodParameters.size(); i++) {
            VariableElement parameter = methodParameters.get(i);

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
            replayOnTargetBuilder.add("($T) $L[$L]", paramType, FIELD_NAME_PARAMS, i);

            if (i < methodParameters.size() - 1) {
                final String separator = ", ";

                allParamNamesBuilder.append(separator);
                allParamTypesBuilder.append(separator);
                replayOnTargetBuilder.add(separator);
            }
        }
        replayOnTargetBuilder.add(");\n");

        String allParamNames = allParamNamesBuilder.toString();
        String allParamTypes = allParamTypesBuilder.toString();

        CodeBlock.Builder methodCode = CodeBlock.builder();
        methodCode.beginControlFlow("if ($L())", METHOD_NAME_IS_DELEGATE_BOUND);
        if (methodReturnsNonVoidValue) {
            methodBuilder.addException(NULL_POINTER_EXCEPTION);
            methodBuilder.addJavadoc("@throws $T if {@link $L} is null\n", NULL_POINTER_EXCEPTION,
                                     FIELD_NAME_DELEGATE_REFERENCE);

            methodCode.addStatement("return this.$L.get().$L($L)", FIELD_NAME_DELEGATE_REFERENCE,
                                    methodName, allParamNames)
                      .nextControlFlow("else")
                      .addStatement("throw new $T($S)", NULL_POINTER_EXCEPTION,
                                    FIELD_NAME_DELEGATE_REFERENCE + " contains a null reference " +
                                            "because it is not bound. This method cannot be " +
                                            "invoked since it returns a non-void value and is " +
                                            "auto-generated")
                      .endControlFlow();
        } else {
            methodCode.addStatement("this.$L.get().$L($L)", FIELD_NAME_DELEGATE_REFERENCE,
                                    methodName, allParamNames);

            if (ReplayType.DELEGATE_AND_REPLAY == replayType) {
                methodCode.endControlFlow();
            }

            if (!ReplayStrategy.NONE.equals(replayStrategy)) {
                if (ReplayType.REPLAY_IF_NO_DELEGATE == replayType) {
                    methodCode.nextControlFlow("else");
                }

                methodCode.add(createActionKey(methodName, methodParameters, allParamTypes, group,
                                               replayStrategy))
                          .add("");
                methodCode.addStatement("$L($L, $L)", METHOD_NAME_ADD_REPLAYABLE_ACTION,
                                        VAR_NAME_ACTION_KEY,
                                        createAnonymousReplayableAction(allParamNames,
                                                                        replayOnTargetBuilder.build()));
            }

            if (ReplayType.REPLAY_IF_NO_DELEGATE == replayType) {
                methodCode.endControlFlow();
            }
        }

        methodBuilder.addCode(methodCode.build());

        return methodBuilder.build();
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

    private CodeBlock createActionKey(String methodName, List<? extends VariableElement> parameters,
                                      String allParamTypes, String group, ReplayStrategy replayStrategy) {
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
                // Generated a method signature with unique parameter values as part of the
                // signature to avoid hashCode or hash collisions. Also this is more readable
                actionKeyCode.add("\"$L(", methodName);

                for (int i = 0; i < parameters.size(); i++) {
                    VariableElement parameter = parameters.get(i);

                    TypeName paramType = TypeName.get(parameter.asType());
                    Name paramName = parameter.getSimpleName();

                    actionKeyCode.add("$L: \" + ", paramType.toString());

                    if (paramType.isPrimitive()) {
                        actionKeyCode.add("$L", paramName);
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


    private TypeSpec createAnonymousReplayableAction(String allParamNames, CodeBlock code) {
        return new ReplayableActionBuilder(targetClassName)
                .constructorArgumentNames(allParamNames)
                .replayOnTargetBody(code)
                .build();
    }


    public List<String> getWarnings() {
        return warnings;
    }

    public List<String> getErrors() {
        return errors;
    }
}