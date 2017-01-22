package com.aniruddhfichadia.replayableinterface;


import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.TypeSpec.Builder;

import javax.lang.model.element.Modifier;

import static com.aniruddhfichadia.replayableinterface.ReplayableInterfaceProcessor.PACKAGE_REPLAYABLE_INTERFACE;
import static com.aniruddhfichadia.replayableinterface.ReplayableInterfaceProcessor.REPLAYABLE_ACTION;
import static com.aniruddhfichadia.replayableinterface.ReplayableInterfaceProcessor.STRING;


/**
 * @author Aniruddh Fichadia
 * @date 21/1/17
 */
public class ReplaySourceBuilder {
    public static final ClassName REPLAY_SOURCE = ClassName.get(PACKAGE_REPLAYABLE_INTERFACE,
                                                                "ReplaySource");

    public static final ClassName LINKED_HASH_MAP = ClassName.get("java.util", "LinkedHashMap");
    public static final ClassName ENTRY           = ClassName.get("java.util.Map", "Entry");

    public static final String FIELD_NAME_ACTIONS = "actions";
    public static final String PARAM_NAME_TARGET  = "target";

    private final TypeSpec.Builder classBuilder;
    private final ClassName        targetClassName;
    private final boolean          clearAfterReplaying;

    private final ClassName typeKey = STRING;
    private final ParameterizedTypeName typeValue;


    public ReplaySourceBuilder(Builder classBuilder, ClassName targetClassName, boolean clearAfterReplaying) {
        super();

        this.classBuilder = classBuilder;
        this.targetClassName = targetClassName;
        this.typeValue = ParameterizedTypeName.get(REPLAYABLE_ACTION, targetClassName);
        this.clearAfterReplaying = clearAfterReplaying;
    }


    public ReplaySourceBuilder applyClassDefinition() {
        classBuilder.addSuperinterface(ParameterizedTypeName.get(REPLAY_SOURCE, targetClassName));
        return this;
    }

    public ReplaySourceBuilder applyFields() {
        classBuilder.addField(createFieldActions());
        return this;
    }

    public ReplaySourceBuilder applyMethods() {
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


    private MethodSpec createMethodReplay() {
        CodeBlock.Builder replayMethodBody =
                CodeBlock.builder()
                         .beginControlFlow("for ($T entry : $L.entrySet())",
                                           ParameterizedTypeName.get(ENTRY, typeKey, typeValue),
                                           FIELD_NAME_ACTIONS)
                         .addStatement("entry.getValue().replayOnTarget($L)", PARAM_NAME_TARGET)
                         .endControlFlow();
        if (clearAfterReplaying) {
            replayMethodBody.addStatement("$L.clear()", FIELD_NAME_ACTIONS);
        }

        return MethodSpec.methodBuilder("replay")
                         .addAnnotation(Override.class)
                         .addModifiers(Modifier.PUBLIC)
                         .addParameter(targetClassName, PARAM_NAME_TARGET)
                         .addCode(replayMethodBody.build())
                         .build();
    }
}