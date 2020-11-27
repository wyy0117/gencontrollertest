package com.wyy.gencontrollertest.reader

import org.springframework.web.bind.annotation.*

import java.beans.PropertyDescriptor
import java.lang.reflect.Field
import java.lang.reflect.Method
import java.lang.reflect.Parameter

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

    final String parameterName() {
        parameter.name
    }

    final GenericClass genericClass() {
        new GenericClass(parameter.parameterizedType)
    }

    /**
     * 本应该通过反射只返回有set方法的属性，但是因为build模式下反射不到所有属性的set方法，java/beans/PropertyDescriptor.java:300
     * 所以暂时返回所有的属性
     * @return
     */
    final List<String> fieldNames() {
        Field[] fields = fields()
        fields*.name
    }

    final Field[] fields() {
        Class<?> clazz = parameter.type
        List<String> setMethodList = clazz.getDeclaredMethods()*.name*.toLowerCase().findAll({ it.startsWith("set") })
        Field[] fields = clazz.declaredFields.findAll({ setMethodList.contains("set" + it.name) })
        //build模式下，无法反射出set方法，坑java/beans/PropertyDescriptor.java:300
//        List<String> fieldNames = fields.find({
//            PropertyDescriptor propertyDescriptor = new PropertyDescriptor(it.name, parameter.getType())
//            Method method = propertyDescriptor.getWriteMethod()
//            method != null
//        }).collect({ it.name })
//        fieldNames
        fields
    }

    /**
     * 只有RequestParam才有defaultValue
     * @return
     */
    final String defaultValue() {
        RequestParam annotation = parameter.getAnnotation(RequestParam.class)
        if (annotation == null || annotation.defaultValue() == ValueConstants.DEFAULT_NONE) {
            return 'null'
        }
        annotation.defaultValue()
    }
}
