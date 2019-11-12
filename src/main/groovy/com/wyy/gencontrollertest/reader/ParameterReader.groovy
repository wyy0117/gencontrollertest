package com.wyy.gencontrollertest.reader


import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl

import java.beans.PropertyDescriptor
import java.lang.reflect.Field
import java.lang.reflect.Method
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
        fieldNames()
        parameter.name
    }

    /**
     * 本应该通过反射只返回有set方法的属性，但是因为build模式下反射不到所有属性的set方法，所以暂时返回所有的属性
     * @return
     */
    final List<String> fieldNames() {
        Class<?> clazz = parameter.type
        Field[] fields = clazz.declaredFields

        //build模式下，无法反射出set方法，坑
//        List<String> fieldNames = fields.find({
//            PropertyDescriptor propertyDescriptor = new PropertyDescriptor(it.name, clazz)
//            Method method = propertyDescriptor.getWriteMethod()
//            method != null
//        }).collect({ it.name })
//        fieldNames
        fields*.name
    }
}
