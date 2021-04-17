package com.wyy.gencontrollertest.generator

import com.wyy.gencontrollertest.config.GeneratorConfig
import com.wyy.gencontrollertest.reader.*
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.multipart.MultipartFile

import java.lang.reflect.Field
import java.lang.reflect.Parameter

/**
 * @Date: 2020/11/25
 * @Author: wyy
 */
class MethodGenerator {

    private GeneratorConfig config

    MethodGenerator(GeneratorConfig config) {
        this.config = config
    }

    def gen() {
        ClassReader classReader = new ClassReader(config.clazz)
        List<AMethod> methods = classReader.methods().collect {
            MethodReader methodReader = new MethodReader(it)
            if (!methodReader.validMethod()) {
                return
            }
            AMethod method = new AMethod(name: methodReader.methodName(), uri: methodReader.url(), requestMethod: methodReader.requestMethod(), returnType: methodReader.returnType(), responseType: methodReader.returnType().clazz.simpleName)

            if (methodReader.returnType().clazz.simpleName != "void") {
                methodReader.returnType().classList().each {
                    ImportQueue.instance.add(it.name)
                }
            }

            Parameter[] parameters = methodReader.parameters()

            parameters.each {
                ParameterReader parameterReader = new ParameterReader(it)
                if (parameterReader.annotation?.annotationType() == RequestBody.class) {
                    //数组或者集合
                    if (it.type.simpleName.contains("[]") || it.type.simpleName == List.class.simpleName) {
                        method.bodyParameterList.add(new AParameter(name: it.name, type: List.class.simpleName))
                        ImportQueue.instance.add(List.class.name)
                        method.bodyType = "List"
                    } else {
                        //1个body，获取类型
                        method.bodyType = it.type.simpleName
                        ImportQueue.instance.add(it.type.name)
                        //body的所有属性
                        Field[] fields = parameterReader.fields()
                        fields.each {
                            method.bodyParameterList.add(new AParameter(name: it.name, type: it.type.simpleName))
                            ImportQueue.instance.add(it.class.name)
                        }
                    }
                } else if (parameterReader.genericClass().clazz == MultipartFile.class) {
                    ImportQueue.instance.add(File.class.getName())
                    method.fileParameter = new AParameter(name: parameterReader.parameterName())
                } else if (parameterReader.annotation?.annotationType() == PathVariable.class) {
                    method.pathParameterList.add(new AParameter(name: parameterReader.parameterName(), type: parameterReader.genericClass().clazz.getSimpleName()))
                } else if (parameterReader.annotation?.annotationType() == RequestParam.class) {
                    method.queryParameterList.add(new AParameter(name: parameterReader.annotation.value() ?: parameterReader.parameterName(), type: parameterReader.genericClass().clazz.getSimpleName(), defaultValue: parameterReader.defaultValue()))
                } else if (parameterReader.annotation?.annotationType() == ModelAttribute.class) {
                    //ModelAttribute的所有属性
                    Field[] fields = parameterReader.fields()
                    fields.each {
                        method.attributeParameterList.add(new AParameter(name: it.name, type: it.type.simpleName))
                        ImportQueue.instance.add(it.class.name)
                    }
                }
            }
            method
        }.findAll({ it != null })

        methods
    }
}
