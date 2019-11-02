package com.wyy.gencontrollertest

import com.wyy.gencontrollertest.exception.NoAnnotationException
import org.springframework.web.bind.annotation.RestController

import java.lang.annotation.Annotation

/**
 * @Date: 19-11-2
 * @Author: wyy
 */
class CodeGenerator {

    private Class aClass

    CodeGenerator(Class aClass) {
        this.aClass = aClass
    }

    void gen() {
        Annotation annotation = aClass.getAnnotation(RestController.class)
        if (annotation == null) {
            throw new NoAnnotationException(RestController.class)
        }

    }
}
