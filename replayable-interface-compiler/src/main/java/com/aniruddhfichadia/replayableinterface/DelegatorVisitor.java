package com.aniruddhfichadia.replayableinterface;


import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import javax.lang.model.element.Modifier;

import static com.aniruddhfichadia.replayableinterface.ReplayableInterfaceProcessor.PACKAGE_REPLAYABLE_INTERFACE;


/**
 * TODO: rename, not really a builder?
 *
 * @author Aniruddh Fichadia
 * @date 21/1/17
 */
public class DelegatorVisitor {
    public static final ClassName DELEGATABLE = ClassName.get(PACKAGE_REPLAYABLE_INTERFACE,
                                                              "Delegator");

    public static final String FIELD_NAME_DELEGATE = "delegate";

    public static final String METHOD_NAME_IS_DELEGATE_BOUND = "isDelegateBound";
    public static final String METHOD_NAME_BIND_DELEGATE     = "bindDelegate";
    public static final String METHOD_NAME_UN_BIND_DELEGATE  = "unBindDelegate";

    private final TypeSpec.Builder classBuilder;
    private final ClassName        targetClassName;


    public DelegatorVisitor(TypeSpec.Builder classBuilder, ClassName targetClassName) {
        super();

        this.classBuilder = classBuilder;
        this.targetClassName = targetClassName;
    }


    public DelegatorVisitor applyClassDefinition() {
        classBuilder.addSuperinterface(ParameterizedTypeName.get(DELEGATABLE, targetClassName));
        return this;
    }

    public DelegatorVisitor applyFields() {
        classBuilder.addField(createFieldDelegate());
        return this;
    }

    public DelegatorVisitor applyMethods() {
        classBuilder.addMethod(createMethodBindDelegate())
                    .addMethod(createMethodUnBindDelegate())
                    .addMethod(createMethodIsDelegateBound());
        return this;
    }


    private FieldSpec createFieldDelegate() {
        return FieldSpec.builder(targetClassName, FIELD_NAME_DELEGATE)
                        .addModifiers(Modifier.PRIVATE)
                        .build();
    }

    private MethodSpec createMethodBindDelegate() {
        return MethodSpec.methodBuilder(METHOD_NAME_BIND_DELEGATE)
                         .addAnnotation(Override.class)
                         .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                         .addParameter(ParameterSpec.builder(targetClassName, FIELD_NAME_DELEGATE)
                                                    .build())
                         .addCode(CodeBlock.builder()
                                           .addStatement("this.$L = $L", FIELD_NAME_DELEGATE,
                                                         FIELD_NAME_DELEGATE)
                                           .build()
                         )
                         .build();
    }

    private MethodSpec createMethodUnBindDelegate() {
        return MethodSpec.methodBuilder(METHOD_NAME_UN_BIND_DELEGATE)
                         .addAnnotation(Override.class)
                         .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                         .addCode(CodeBlock.builder()
                                           .addStatement("this.$L = null", FIELD_NAME_DELEGATE)
                                           .build()
                         )
                         .build();
    }

    private MethodSpec createMethodIsDelegateBound() {
        return MethodSpec.methodBuilder(METHOD_NAME_IS_DELEGATE_BOUND)
                         .addAnnotation(Override.class)
                         .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                         .returns(TypeName.BOOLEAN)
                         .addCode(CodeBlock.builder()
                                           .addStatement("return this.$L != null",
                                                         FIELD_NAME_DELEGATE)
                                           .build()
                         )
                         .build();
    }
}