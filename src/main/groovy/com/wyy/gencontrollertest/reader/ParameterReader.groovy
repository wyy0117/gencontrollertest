package com.wyy.gencontrollertest.reader


import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl

import java.lang.reflect.Parameter
import java.lang.reflect.Type

/**
 * @Date: 19-11-3
 * @Author: wyy
 */
class ParameterReader {

    private Parameter parameter
    def annotation

    ParameterReader(Parameter parameter) {
        this.parameter = parameter
        annotation = parameter.getAnnotation(RequestBody.class)
                ?: parameter.getAnnotation(RequestParam.class)
                ?: parameter.getAnnotation(PathVariable.class)
                ?: parameter.getAnnotation(ModelAttribute.class)
    }

    /**
     * 真实类型
     * @return
     */
    final Class<?> type() {
        parameter.type
    }

    /**
     * 泛型类型
     * @return
     */
    final Class<?>[] actualTypes() {
        Type parameterizedType = parameter.getParameterizedType()
        if (parameterizedType != null) {
            if (parameterizedType instanceof ParameterizedTypeImpl) {
                return (Class<?>[]) parameterizedType.actualTypeArguments
            }
        }
        return []
    }

    final String parameterName() {
        parameter.name
    }
}
