package com.wyy.gencontrollertest.reader

import org.springframework.web.bind.annotation.*
import sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl

import java.lang.reflect.Method
import java.lang.reflect.Parameter
import java.lang.reflect.Type

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

    /**
     * 方法名
     * @return
     */
    final String methodName() {
        method.name
    }

    final String url() {
        String url = annotation.value()[0] - '/'
        url.contains("{") && (url = url.substring(0, url.indexOf("{")))
        url
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
            return ((RequestMapping) annotation).method()[0].toString().toLowerCase()
        }
        ((annotation).annotationType().simpleName - 'Mapping').toString().toLowerCase()
    }

    final Parameter[] parameters() {
        method.parameters

    }

    /**
     * 方法的返回类型
     * @return
     */
    final Class<?> returnType() {
        method.returnType
    }

    /**
     * 泛型类型
     * @return
     */
    final Class<?>[] actualTypes() {
        Type parameterizedType = method.genericReturnType
        if (parameterizedType != null) {
            if (parameterizedType instanceof ParameterizedTypeImpl) {
                return (Class<?>[]) parameterizedType.actualTypeArguments
            }
        }
        return []
    }
}
