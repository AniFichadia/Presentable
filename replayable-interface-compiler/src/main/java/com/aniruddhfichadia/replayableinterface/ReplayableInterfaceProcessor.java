package com.aniruddhfichadia.replayableinterface;


import com.google.auto.common.SuperficialValidation;
import com.google.auto.service.AutoService;

import java.lang.annotation.Annotation;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;


/**
 * @author Aniruddh Fichadia
 * @date 18/1/17
 */
@AutoService(Processor.class)
public class ReplayableInterfaceProcessor
        extends AbstractProcessor {
    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> types = new LinkedHashSet<>();
        for (Class<? extends Annotation> annotation : getSupportedAnnotations()) {
            types.add(annotation.getCanonicalName());
        }
        return types;
    }


    private Set<Class<? extends Annotation>> getSupportedAnnotations() {
        Set<Class<? extends Annotation>> annotations = new LinkedHashSet<>();

        annotations.add(ReplayableInterface.class);

        return annotations;
    }


    @Override
    public boolean process(Set<? extends TypeElement> elements, RoundEnvironment env) {
        // Process each @ReplayableInterface element.
        for (Element element : env.getElementsAnnotatedWith(ReplayableInterface.class)) {
            if (!SuperficialValidation.validateElement(element)) {
                continue;
            }

            if (ElementKind.INTERFACE.equals(element.getKind())) {
                ReplayableInterface classAnnotation = element.getAnnotation(ReplayableInterface.class);


                // TODO generate
            }
        }

        return false;
    }
}