package com.aniruddhfichadia.replayableinterface;


import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;

import javax.lang.model.element.Modifier;

import static com.aniruddhfichadia.replayableinterface.ReplayableInterfaceProcessor.PACKAGE_REPLAYABLE_INTERFACE;


/**
 * @author Aniruddh Fichadia
 * @date 23/1/17
 */
public class ReplayableActionBuilder {
    public static final ClassName REPLAYABLE_ACTION = ClassName.get(PACKAGE_REPLAYABLE_INTERFACE,
                                                                    "ReplayableAction");

    public static final String METHOD_NAME_REPLAY_ON_TARGET = "replayOnTarget";
    public static final String FIELD_NAME_PARAMS            = "params";
    public static final String PARAM_NAME_TARGET            = "target";

    private final ClassName targetClassName;

    private String    constructorArgumentNames;
    private CodeBlock replayOnTargetBody;


    public ReplayableActionBuilder(ClassName targetClassName) {
        this.targetClassName = targetClassName;
    }

    public ReplayableActionBuilder constructorArgumentNames(String constructorArgumentNames) {
        this.constructorArgumentNames = constructorArgumentNames;
        return this;
    }

    public ReplayableActionBuilder replayOnTargetBody(CodeBlock replayOnTargetBody) {
        this.replayOnTargetBody = replayOnTargetBody;
        return this;
    }


    public TypeSpec build() {
        return TypeSpec.anonymousClassBuilder(constructorArgumentNames)
                       .superclass(ParameterizedTypeName.get(REPLAYABLE_ACTION, targetClassName))
                       .addMethod(MethodSpec.methodBuilder(METHOD_NAME_REPLAY_ON_TARGET)
                                            .addAnnotation(Override.class)
                                            .addModifiers(Modifier.PUBLIC)
                                            .addParameter(targetClassName, PARAM_NAME_TARGET)
                                            .addCode(replayOnTargetBody)
                                            .build())
                       .build();
    }

}