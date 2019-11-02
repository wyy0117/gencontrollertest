package com.wyy.gencontrollertest.exception

/**
 * @Date: 19-11-2
 * @Author: wyy
 */
class NoAnnotationException extends Exception {

    NoAnnotationException(Class annotation) {
        super("no annotation ${annotation} exist")
    }
}
