package com.wyy.gencontrollertest.reader

import org.springframework.web.bind.annotation.RequestMapping

import java.lang.reflect.Method

/**
 * @Date: 19-11-3
 * @Author: wyy
 */
class ClassReader {

    private Class<?> clazz = null

    ClassReader(Class clazz) {
        this.clazz = clazz
    }

    String className() {
        clazz.simpleName
    }

    final Method[] methods() {
        clazz.declaredMethods
    }

    final String url() {
        RequestMapping annotation = clazz.getAnnotation(RequestMapping.class)
        if (annotation != null) {
            String[] urls = annotation.value()
            if (urls.length > 0) {
                String url = urls[0].substring(1)
                '/' + url
            }

        } else {
            ""
        }
    }
}
