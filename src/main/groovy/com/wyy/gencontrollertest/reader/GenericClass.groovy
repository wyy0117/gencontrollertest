package com.wyy.gencontrollertest.reader

import sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl

import java.lang.reflect.Type
import java.lang.reflect.TypeVariable

/**
 * @Date: 19-11-21
 * @Author: wyy
 */
/**
 * 包含泛型类型的类
 */
class GenericClass {

    /**
     * 真实类
     */
    Class<?> clazz

    /**
     * 泛型类型
     */
    List<GenericClass> genericList = new ArrayList<>()

    GenericClass(Class<?> clazz) {
        this.clazz = clazz
    }

    GenericClass(ParameterizedTypeImpl parameterizedType) {
        this.clazz = parameterizedType.rawType
        Type[] actualTypeArguments = parameterizedType.actualTypeArguments
        genericList = actualTypeArguments.collect({
            if (it instanceof Class || it instanceof ParameterizedTypeImpl) {
                new GenericClass(it)
            } else {
                return
            }
        })
    }

    @Override
    String toString() {
        StringBuilder stringBuilder = new StringBuilder()
        stringBuilder.append(clazz.simpleName)
        if (genericList.size() == 0) {
            return stringBuilder.toString()
        } else {
            stringBuilder.append("<")
            StringBuilder genericBuilder = new StringBuilder()
            genericList.each {
                genericBuilder.append(it.toString())
                genericBuilder.append(",")
            }
            genericBuilder.deleteCharAt(genericBuilder.length() - 1)
            stringBuilder.append(genericBuilder)

            stringBuilder.append(">")
        }
    }

    Set<Class> classList() {
        Set<Class> list = []
        list.add(clazz)
        genericList.each {
            list.addAll(it.classList())
        }
        return list
    }
}
