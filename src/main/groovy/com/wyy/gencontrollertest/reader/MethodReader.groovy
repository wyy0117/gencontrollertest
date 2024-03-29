package com.wyy.gencontrollertest.reader

import org.springframework.web.bind.annotation.*

import java.lang.annotation.AnnotationFormatError
import java.lang.reflect.Method
import java.lang.reflect.Parameter

/**
 * @Date: 19-11-3
 * @Author: wyy
 */
class MethodReader {

    private Method method
    private def annotation
    private GenericClass genericClass
    private String classMappingPath = ""
    private Class methodClass

    MethodReader(Method method) {
        this.method = method
        annotation = method.getAnnotation(RequestMapping.class)
                ?: method.getAnnotation(PostMapping.class)
                ?: method.getAnnotation(PutMapping.class)
                ?: method.getAnnotation(GetMapping.class)
                ?: method.getAnnotation(DeleteMapping.class)

        genericClass = new GenericClass(method.genericReturnType)

        methodClass = method.declaringClass
        def classAnnotation = methodClass.getAnnotation(RequestMapping.class)
                ?: methodClass.getAnnotation(PostMapping.class)
                ?: methodClass.getAnnotation(PutMapping.class)
                ?: methodClass.getAnnotation(GetMapping.class)
                ?: methodClass.getAnnotation(DeleteMapping.class)

        def values = classAnnotation?.value()
        String path
        if (values != null && values.size() > 0 && (path = values[0]).size() > 0) {
            classMappingPath = path
            if (!classMappingPath.startsWith("/")) {
                classMappingPath = "/" + classMappingPath
            }
        }
    }

    /**
     * 方法名
     * @return
     */
    final String methodName() {
        method.name
    }

    final String url() {

        String url = annotation.value()[0]
        if (!url.startsWith("/")) {
            url = "/" + url
        }
        classMappingPath + url
    }

    /**
     * 是否添加了接口的注解
     * @return
     */
    final boolean validMethod() {
        annotation != null
    }

    /**
     * 接口的请求方法
     * @return
     */
    final String requestMethod() {
        if (annotation instanceof RequestMapping) {
            //如果方法上反射出的注解无请求方式的生命则反射出类上面的RequestMapping注解
            RequestMethod[] requestMethods = ((RequestMapping) annotation).method() ?: this.method.getDeclaringClass().getAnnotation(RequestMapping.class).method()
            if (requestMethods.size() == 0) {
                throw new AnnotationFormatError("${this.method} have no request method found")
            }
            return requestMethods[0].toString().toLowerCase()
        }
        ((annotation).annotationType().simpleName - 'Mapping').toString().toLowerCase()
    }

    final Parameter[] parameters() {
        method.parameters
    }

    final GenericClass returnType() {
        genericClass
    }
}
