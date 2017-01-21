package com.aniruddhfichadia.replayableinterface;


import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;

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

    public static final String fieldNameActions = "actions";
    public static final String paramNameTarget  = "classBuilder";

    private final TypeSpec.Builder classBuilder;
    private final ClassName        targetClassName;

    private final ClassName typeKey = STRING;
    private final ParameterizedTypeName typeValue;


    public ReplaySourceBuilder(TypeSpec.Builder classBuilder, ClassName targetClassName) {
        super();

        this.classBuilder = classBuilder;
        this.targetClassName = targetClassName;
        this.typeValue = ParameterizedTypeName.get(REPLAYABLE_ACTION, targetClassName);
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
                                 fieldNameActions)
                        .addModifiers(Modifier.PRIVATE, Modifier.FINAL)
                        .initializer(CodeBlock.of("new LinkedHashMap<>()"))
                        .build();
    }


    private MethodSpec createMethodReplay() {
        return MethodSpec.methodBuilder("replay")
                         .addAnnotation(Override.class)
                         .addModifiers(Modifier.PUBLIC)
                         .addParameter(targetClassName, paramNameTarget)
                         .addCode(CodeBlock.builder()
                                           .beginControlFlow(
                                                   "for ($T entry : $L.entrySet())",
                                                   ParameterizedTypeName.get(ENTRY, typeKey,
                                                                             typeValue),
                                                   fieldNameActions)
                                           .addStatement("entry.getValue().replayOnTarget($L)",
                                                         paramNameTarget)
                                           .endControlFlow()
                                           .addStatement("$L.clear()", fieldNameActions)
                                           .build()
                         )
                         .build();
    }
}