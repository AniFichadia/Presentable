package com.aniruddhfichadia.replayableinterface;


import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.TypeSpec.Builder;

import javax.lang.model.element.Modifier;

import static com.aniruddhfichadia.replayableinterface.ReplayableActionBuilder.METHOD_NAME_REPLAY_ON_TARGET;
import static com.aniruddhfichadia.replayableinterface.ReplayableActionBuilder.REPLAYABLE_ACTION;
import static com.aniruddhfichadia.replayableinterface.ReplayableInterfaceProcessor.PACKAGE_REPLAYABLE_INTERFACE;
import static com.aniruddhfichadia.replayableinterface.ReplayableInterfaceProcessor.STRING;


/**
 * @author Aniruddh Fichadia
 * @date 21/1/17
 */
public class ReplaySourceVisitor {
    public static final ClassName REPLAY_SOURCE = ClassName.get(PACKAGE_REPLAYABLE_INTERFACE,
                                                                "ReplaySource");

    public static final ClassName LINKED_HASH_MAP = ClassName.get("java.util", "LinkedHashMap");
    public static final ClassName ENTRY           = ClassName.get("java.util.Map", "Entry");

    public static final String FIELD_NAME_ACTIONS                = "actions";
    public static final String METHOD_NAME_ADD_REPLAYABLE_ACTION = "addReplayableAction";
    public static final String METHOD_NAME_REPLAY                = "replay";
    public static final String PARAM_NAME_KEY                    = "key";
    public static final String PARAM_NAME_ACTION                 = "action";
    public static final String PARAM_NAME_TARGET                 = "target";

    private final TypeSpec.Builder classBuilder;
    private final ClassName        targetClassName;
    private final boolean          clearAfterReplaying;

    private final ClassName typeKey = STRING;
    private final ParameterizedTypeName typeValue;


    public ReplaySourceVisitor(Builder classBuilder, ClassName targetClassName, boolean clearAfterReplaying) {
        super();

        this.classBuilder = classBuilder;
        this.targetClassName = targetClassName;
        this.typeValue = ParameterizedTypeName.get(REPLAYABLE_ACTION, targetClassName);
        this.clearAfterReplaying = clearAfterReplaying;
    }


    public ReplaySourceVisitor applyClassDefinition() {
        classBuilder.addSuperinterface(ParameterizedTypeName.get(REPLAY_SOURCE, targetClassName));
        return this;
    }

    public ReplaySourceVisitor applyFields() {
        classBuilder.addField(createFieldActions());
        return this;
    }

    public ReplaySourceVisitor applyMethods() {
        classBuilder.addMethod(createMethodAddReplayableAction());
        classBuilder.addMethod(createMethodReplay());
        return this;
    }


    private FieldSpec createFieldActions() {
        return FieldSpec.builder(ParameterizedTypeName.get(LINKED_HASH_MAP, typeKey, typeValue),
                                 FIELD_NAME_ACTIONS)
                        .addModifiers(Modifier.PRIVATE, Modifier.FINAL)
                        .initializer(CodeBlock.of("new LinkedHashMap<>()"))
                        .build();
    }


    private MethodSpec createMethodAddReplayableAction() {
        return MethodSpec.methodBuilder(METHOD_NAME_ADD_REPLAYABLE_ACTION)
                         .addAnnotation(Override.class)
                         .addModifiers(Modifier.PUBLIC)
                         .addParameter(STRING, PARAM_NAME_KEY)
                         .addParameter(
                                 ParameterizedTypeName.get(REPLAYABLE_ACTION, targetClassName),
                                 PARAM_NAME_ACTION)
                         .addCode(CodeBlock.builder()
                                           .addStatement("this.$L.remove($L)", FIELD_NAME_ACTIONS,
                                                         PARAM_NAME_KEY)
                                           .addStatement("this.$L.put($L, $L)", FIELD_NAME_ACTIONS,
                                                         PARAM_NAME_KEY, PARAM_NAME_ACTION)
                                           .build())
                         .build();
    }

    private MethodSpec createMethodReplay() {
        CodeBlock.Builder replayMethodBody =
                CodeBlock.builder()
                         .beginControlFlow("for ($T entry : $L.entrySet())",
                                           ParameterizedTypeName.get(ENTRY, typeKey, typeValue),
                                           FIELD_NAME_ACTIONS)
                         .addStatement("entry.getValue().$L($L)", METHOD_NAME_REPLAY_ON_TARGET,
                                       PARAM_NAME_TARGET)
                         .endControlFlow();
        if (clearAfterReplaying) {
            replayMethodBody.addStatement("$L.clear()", FIELD_NAME_ACTIONS);
        }

        return MethodSpec.methodBuilder(METHOD_NAME_REPLAY)
                         .addAnnotation(Override.class)
                         .addModifiers(Modifier.PUBLIC)
                         .addParameter(targetClassName, PARAM_NAME_TARGET)
                         .addCode(replayMethodBody.build())
                         .build();
    }
}