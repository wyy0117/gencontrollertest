package com.wyy.gencontrollertest.reader

import org.springframework.web.bind.annotation.*

import java.lang.reflect.Method
import java.lang.reflect.Parameter

/**
 * @Date: 19-11-3
 * @Author: wyy
 */
class MethodReader {

    private Method method
    private def annotation

    MethodReader(Method method) {
        this.method = method
        annotation = method.getAnnotation(RequestMapping.class)
                ?: method.getAnnotation(PostMapping.class)
                ?: method.getAnnotation(PutMapping.class)
                ?: method.getAnnotation(GetMapping.class)
                ?: method.getAnnotation(DeleteMapping.class)
    }

    final String methodName() {
        method.name
    }

    final String url() {
        String url = annotation.value()[0] - '/'
        url.contains("{") && (url = url.substring(0, url.indexOf("{")))
        url
    }

    final String requestMethod() {
        if (annotation instanceof RequestMapping) {
            return ((RequestMapping) annotation).method()[0].toString().toLowerCase()
        }
        ((annotation).annotationType().simpleName - 'Mapping').toString().toLowerCase()
    }

    final Parameter[] parameters() {
        method.parameters

    }

    final Class returnType() {
        method.returnType
    }
}
