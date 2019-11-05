package com.wyy.gencontrollertest.reader

import org.springframework.core.LocalVariableTableParameterNameDiscoverer
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam

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

    final Class type() {
        parameter.type
    }


    final String key() {
        LocalVariableTableParameterNameDiscoverer u =
                new LocalVariableTableParameterNameDiscoverer()

        parameter.name
    }
}
