package com.wyy.gencontrollertest.generator.method.test

import com.wyy.gencontrollertest.config.AuthType
import com.wyy.gencontrollertest.config.ConfigConstant
import com.wyy.gencontrollertest.config.GeneratorConfig
import com.wyy.gencontrollertest.generator.prefix.ImportQueue
import com.wyy.gencontrollertest.reader.MethodReader
import com.wyy.gencontrollertest.reader.ParameterReader
import io.restassured.http.ContentType
import io.restassured.specification.RequestSpecification
import org.junit.Test
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.multipart.MultipartFile

import java.lang.reflect.Method
import java.lang.reflect.Parameter

/**
 * @Date: 19-11-4
 * @Author: wyy
 */
abstract class TestAbstractGenerator implements ITestGenerator {

    private final String REQUEST_PARAMETER_MAP = 'requestParameter'
    private final String REQUEST = "request"

    private Method method
    private MethodReader methodReader
    private GeneratorConfig config

    TestAbstractGenerator(Method method, GeneratorConfig config) {
        this.method = method
        this.config = config
        methodReader = new MethodReader(method)
    }

    @Override
    String requestMethod() {
        methodReader.requestMethod()
    }

    @Override
    Class returnType() {

        Class<?> returnType = methodReader.returnType()
        ImportQueue.instance.add(returnType.name)
        returnType
    }

    @Override
    String name() {
        methodReader.methodName()
    }

    @Override
    String url() {
        methodReader.url()
    }

    @Override
    StringBuilder gen() {

        if (!methodReader.validMethod()) {
            new StringBuilder()
        }
        //测试的入口
        ImportQueue.instance.add(Test.class.name)
        StringBuilder builder = new StringBuilder()
        builder.append("@Test\n")
        builder.append("void ${name()}Test() {\n")

        StringBuilder declareVariableBuilder = new StringBuilder()//声明变量

        // 定义一个map，作为requestParameter的声明
        StringBuilder requestParameterMap = new StringBuilder("Map ${REQUEST_PARAMETER_MAP} = [\n")

        StringBuilder invokeBuilder = new StringBuilder()//调用方法
        StringBuilder parametersBuilder = new StringBuilder()//方法定义参数
        StringBuilder pathParametersBuilder = new StringBuilder()//path，调接口时使用

        boolean haveBody = false
        String bodyName = ''

        boolean haveFile = false
        boolean haveFiles = false
        String fileName = ""

        boolean haveForm = false
        String formName = ''

        boolean haveReqeustParameter = false

        Parameter[] parameters = methodReader.parameters()
        parameters.each {
            ParameterReader parameterReader = new ParameterReader(it)
            def annotation = parameterReader.annotation
            if (annotation instanceof PathVariable) {
                pathVariable(declareVariableBuilder, invokeBuilder, parametersBuilder, pathParametersBuilder, parameterReader.type(), parameterReader.key())
            } else if (annotation instanceof RequestParam) {
                haveReqeustParameter = true
                requestParameter(requestParameterMap, parameterReader.key())
            } else if (annotation instanceof RequestBody) {
                haveBody = true
                bodyName = parameterReader.key()
                requestBody(declareVariableBuilder, invokeBuilder, parametersBuilder, parameterReader.type(), parameterReader.key())
            } else if (annotation instanceof ModelAttribute) {
                formName = parameterReader.key()
                modelAttribute(declareVariableBuilder, invokeBuilder, parametersBuilder, parameterReader.key())
            } else if (parameterReader.type().name == MultipartFile[].class.name) {
                haveFiles = true
                haveForm = true
                ImportQueue.instance.add(File.class.getName())
                fileName = parameterReader.key()
                declareVariableBuilder.append("File[] ${parameterReader.key()} = []")
                invokeBuilder.append("${parameterReader.key()},")
                parametersBuilder.append("File[] ${parameterReader.key()},")
            } else if (parameterReader.type().name == MultipartFile.class.name) {
                haveFile = true
                haveForm = true
                ImportQueue.instance.add(File.class.name)
                fileName = parameterReader.key()
                declareVariableBuilder.append("File ${parameterReader.key()} = new File()")
                invokeBuilder.append("${parameterReader.key()},")
                parametersBuilder.append("File ${parameterReader.key()},")
            } else {
                println "${name()} parameter ${parameterReader.key()} have no annotation"
            }
        }
        haveReqeustParameter && parametersBuilder.append("Map ${REQUEST_PARAMETER_MAP},")
        haveReqeustParameter && invokeBuilder.append(REQUEST_PARAMETER_MAP)

        declareVariableBuilder = new StringBuilder(declareVariableBuilder.toString().split(",").join(","))
        requestParameterMap = new StringBuilder(requestParameterMap.toString().split(",").join(","))
        invokeBuilder = new StringBuilder(invokeBuilder.toString().split(",").join(","))
        parametersBuilder = new StringBuilder(parametersBuilder.toString().split(",").join(","))
        pathParametersBuilder = new StringBuilder(pathParametersBuilder.toString().split(",").join(","))

        requestParameterMap.append("]\n")//requestMap最后拼接】

        builder.append(declareVariableBuilder)
        haveReqeustParameter && builder.append(requestParameterMap)
        builder.append("\n${name()}(")
        invokeBuilder.length() > 0 && builder.append("${invokeBuilder.toString()}")

        builder.append(")\n}\n")

        //测试的真实方法
        builder.append("private ${returnType().simpleName} ${name()}(")
        builder.append(parametersBuilder)

        builder.append("){\n")
        ImportQueue.instance.add(RequestSpecification.class.name)
        builder.append("RequestSpecification ${REQUEST} = given()\n")
        if (config.authType == AuthType.JWT) {
            builder.append(".header('${ConfigConstant.AUTHORIZATION}', ${ConfigConstant.JWT_TOKEN})\n")
        } else if (config.authType == AuthType.BASIC) {
            builder.append(".auth().preemptive().basic(\"${ConfigConstant.USERNAME}\", \"${ConfigConstant.PASSWORD}\")\n")
        }
        if (haveFile) {
            builder.append(".multiPart('${fileName}',${fileName})\n")
        } else if (haveFiles) {
            builder.append(".multiPart('${fileName}',${fileName}[0])\n")
        }
        if (haveBody) {
            ImportQueue.instance.add(ContentType.class.name)
            builder.append('.contentType(ContentType.JSON)\n')
            builder.append(".body(${bodyName})\n")
        }

        if (haveForm) {
            builder.append("${formName}.each {\n")
            builder.append("${REQUEST}.multiPart(it.key, it.value)\n")
            builder.append("}\n")
        }
        if (haveReqeustParameter) {
            builder.append("${REQUEST}.${requestMethod()}('${url()}' , $REQUEST_PARAMETER_MAP)\n")
        } else {
            if (pathParametersBuilder.length() > 0) {
                builder.append("${REQUEST}.${requestMethod()}('${url()}',${pathParametersBuilder.toString()})\n")
            } else {
                builder.append("${REQUEST}.${requestMethod()}('${url()}')\n")
            }
        }

        if (returnType().name == void.class.name) {
            builder.append(".then()\n")
            builder.append(".statusCode(200)\n}\n")
        } else {
            builder.append(".as(${returnType().simpleName}.class)\n}\n\n")
        }
        builder
    }

    //肯定是java.lang 中的类，不需要import
    protected void pathVariable(StringBuilder declareVariableBuilder, StringBuilder invokeBuilder, StringBuilder parametersBuilder, StringBuilder pathParametersBuilder, Class aClass, String name) {
        declareVariableBuilder.append("${aClass.simpleName} ${name} = new Object()\n")
        invokeBuilder.append("${name},")
        parametersBuilder.append("${aClass.simpleName} ${name},")
        pathParametersBuilder.append("${name},")
    }

    //肯定是java.lang 中的类，不需要import
    protected void requestParameter(StringBuilder declareMapBuilder, String name) {
        declareMapBuilder.append("${name}:new Object(),\n")
    }

    protected void requestBody(StringBuilder declareVariableBuilder, StringBuilder invokeBuilder, StringBuilder parametersBuilder, Class aClass, String name) {
        ImportQueue.instance.add(aClass.name)
        if (aClass.isInterface()) {
            declareVariableBuilder.append("${aClass.simpleName} ${name} = new Object()\n")
        } else {
            declareVariableBuilder.append("${aClass.simpleName} ${name} = new ${aClass.simpleName}()\n")
        }
        invokeBuilder.append("$name ,")
        parametersBuilder.append("${aClass.simpleName} ${name},")
    }

    protected void modelAttribute(StringBuilder declareVariableBuilder, StringBuilder invokeBuilder, StringBuilder parametersBuilder, String name) {
        declareVariableBuilder.append("Map ${name} = [:]\n")
        invokeBuilder.append("$name ,")
        parametersBuilder.append("Map ${name},")
    }
}
