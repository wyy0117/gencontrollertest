package com.wyy.gencontrollertest.generator.method.test

import com.wyy.gencontrollertest.config.AuthType
import com.wyy.gencontrollertest.config.ConfigConstant
import com.wyy.gencontrollertest.config.GeneratorConfig
import com.wyy.gencontrollertest.generator.prefix.ImportQueue
import com.wyy.gencontrollertest.reader.GenericClass
import com.wyy.gencontrollertest.reader.MethodReader
import com.wyy.gencontrollertest.reader.ParameterReader
import io.restassured.RestAssured
import io.restassured.config.EncoderConfig
import io.restassured.http.ContentType
import io.restassured.response.Response
import io.restassured.response.Validatable
import io.restassured.response.ValidatableResponse
import io.restassured.specification.RequestSpecification
import org.junit.Test
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.multipart.MultipartFile

import java.lang.reflect.Method
import java.lang.reflect.Parameter
import java.nio.charset.Charset

/**
 * @Date: 19-11-4
 * @Author: wyy
 */
abstract class TestAbstractGenerator implements ITestGenerator {

    private final String QUERY_MAP = 'queryMap'
    private final String REQUEST = "request"
    private final String RESULT = 'result'
    private final String RESPONSE = 'response'

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
    GenericClass returnType() {
        methodReader.genericClass()
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

        StringBuilder queryParameterBuilder = new StringBuilder("Map $QUERY_MAP = [\n")
        StringBuilder formParameterBuilder = new StringBuilder("")

        StringBuilder invokeBuilder = new StringBuilder()//调用方法
        StringBuilder parametersBuilder = new StringBuilder()//方法定义参数
        StringBuilder pathParametersBuilder = new StringBuilder()//path，调接口时使用

        boolean haveBody = false
        String bodyName = ''

        boolean haveFile = false
        boolean haveFiles = false
        String fileName = ""

        String formName = ''

        boolean haveQueryParameter = false
        boolean haveFormParameter = false

        Parameter[] parameters = methodReader.parameters()
        parameters.each {
            ParameterReader parameterReader = new ParameterReader(it)
            def annotation = parameterReader.annotation
            if (parameterReader.genericClass().clazz.name == MultipartFile[].class.name) {
                haveFiles = true
                ImportQueue.instance.add(File.class.getName())
                fileName = parameterReader.parameterName()
                declareVariableBuilder.append("File[] ${parameterReader.parameterName()} = []\n")
                invokeBuilder.append("${parameterReader.parameterName()},")
                parametersBuilder.append("File[] ${parameterReader.parameterName()},")
            } else if (parameterReader.genericClass().clazz.name == MultipartFile.class.name) {
                haveFile = true
                ImportQueue.instance.add(File.class.name)
                fileName = parameterReader.parameterName()
                declareVariableBuilder.append("File ${parameterReader.parameterName()} = new File('')\n")
                invokeBuilder.append("${parameterReader.parameterName()},")
                parametersBuilder.append("File ${parameterReader.parameterName()},")
            } else if (annotation instanceof PathVariable) {
                pathVariable(declareVariableBuilder, invokeBuilder, parametersBuilder, pathParametersBuilder, parameterReader)
            } else if (annotation instanceof RequestParam) {
                haveQueryParameter = true
                requestParameter(queryParameterBuilder, parameterReader)
            } else if (annotation instanceof RequestBody) {
                haveBody = true
                bodyName = parameterReader.parameterName()
                requestBody(declareVariableBuilder, invokeBuilder, parametersBuilder, parameterReader)
            } else if (annotation instanceof ModelAttribute) {
                haveFormParameter = true
                formName = parameterReader.parameterName()
                modelAttribute(formParameterBuilder, invokeBuilder, parametersBuilder, parameterReader)
            } else {
                println "${name()} parameter ${parameterReader.parameterName()} have no annotation"
            }
        }

        if (haveQueryParameter) {
            invokeBuilder.append("${QUERY_MAP},")
            parametersBuilder.append("${QUERY_MAP},")
        }

        declareVariableBuilder = new StringBuilder(declareVariableBuilder.toString().split(",").join(","))
        invokeBuilder = new StringBuilder(invokeBuilder.toString().split(",").join(","))
        parametersBuilder = new StringBuilder(parametersBuilder.toString().split(",").join(","))
        pathParametersBuilder = new StringBuilder(pathParametersBuilder.toString().split(",").join(","))

        queryParameterBuilder.append("]\n")//requestMap最后拼接】

        builder.append(declareVariableBuilder)
        haveFormParameter && builder.append(formParameterBuilder)
        haveQueryParameter && builder.append(queryParameterBuilder)

        if (returnType().clazz.simpleName != "void") {
            builder.append("\n${returnType().toString()}")
            returnType().classList().each {
                ImportQueue.instance.add(it.name)
            }

            builder.append(" $RESULT = ${name()}(")
            invokeBuilder.length() > 0 && builder.append("${invokeBuilder.toString()}")

            builder.append(")\n")
            builder.append("println gson.toJson($RESULT)\n")
        } else {
            builder.append("\n${name()}(")
            invokeBuilder.length() > 0 && builder.append("${invokeBuilder.toString()}")
            builder.append(")\n")
        }
        builder.append("}\n\n")

        //测试的真实方法
        builder.append("private ${returnType().toString()}")
        builder.append(" ${name()}(")
        builder.append(parametersBuilder)

        builder.append("){\n")
        ImportQueue.instance.add(RequestSpecification.class.name)
        builder.append("RequestSpecification ${REQUEST} = given()\n")
        appendCharset(builder)
        appendAuth(builder)
        appendFile(haveFile, builder, fileName, haveFiles)

        appendBody(haveBody, builder, bodyName)
        appendQueryParameter(haveQueryParameter, builder)
        appendFormParameter(haveFormParameter, builder, formName)

        ImportQueue.instance.add(Validatable.class.name, ValidatableResponse.class.name, Response.class.name)
        if (pathParametersBuilder.length() > 0) {
            builder.append("${Validatable.class.simpleName}<${ValidatableResponse.class.simpleName}, ${Response.class.simpleName}> ${RESPONSE} = ${REQUEST}.${requestMethod()}('${url()}',${pathParametersBuilder.toString()})\n")
        } else {
            builder.append("${Validatable.class.simpleName}<${ValidatableResponse.class.simpleName}, ${Response.class.simpleName}> ${RESPONSE} = ${REQUEST}.${requestMethod()}('${url()}')\n")
        }
        builder.append("${RESPONSE}.then().statusCode(200)\n")

        if (returnType().clazz.name != void.class.name) {
            builder.append("${RESPONSE}.as(${returnType().clazz.simpleName}.class)\n")
        }
        builder.append("}\n")
        builder
    }

    private void appendFormParameter(boolean haveFormParameter, StringBuilder builder, String formName) {
        if (haveFormParameter) {
            builder.append("${formName}.each{\n")
            builder.append("${REQUEST}.formParam(it.key,it.value)")
            builder.append("}\n")
        }
    }

    private void appendQueryParameter(boolean haveQueryParameter, StringBuilder builder) {
        if (haveQueryParameter) {
            builder.append("${QUERY_MAP}.each{\n")
            builder.append("${REQUEST}.queryParam(it.key,it.value)")
            builder.append("}\n")
        }
    }

    private void appendBody(boolean haveBody, StringBuilder builder, String bodyName) {
        if (haveBody) {
            ImportQueue.instance.add(ContentType.class.name)
            builder.append('.contentType(ContentType.JSON)\n')
            builder.append(".body(${bodyName})\n")
        }
    }

    private void appendFile(boolean haveFile, StringBuilder builder, String fileName, boolean haveFiles) {
        if (haveFile) {
            builder.append(".multiPart('${fileName}',${fileName})\n")
        } else if (haveFiles) {
            builder.append("${fileName}.each{\n")
            builder.append("${REQUEST}.multiPart('${fileName}',it)\n")
            builder.append('}\n')
        }
    }

    private void appendAuth(StringBuilder builder) {
        if (config.authType == AuthType.JWT) {
            builder.append(".header('${ConfigConstant.AUTHORIZATION}', ${ConfigConstant.JWT_TOKEN})\n")
        } else if (config.authType == AuthType.BASIC) {
            builder.append(".auth().preemptive().basic(\"${ConfigConstant.USERNAME}\", \"${ConfigConstant.PASSWORD}\")\n")
        }
    }

    private void appendCharset(StringBuilder builder) {
        if (config.charset) {
            ImportQueue.instance.add(RestAssured.class.name, EncoderConfig.class.name, Charset.class.name)
            builder.append(".config(config().encoderConfig(EncoderConfig.encoderConfig().defaultContentCharset(Charset.forName(\"${config.charset}\"))))\n")
        }
    }

    //肯定是java.lang 中的类，不需要import
    protected void pathVariable(StringBuilder declareVariableBuilder, StringBuilder invokeBuilder, StringBuilder parametersBuilder, StringBuilder pathParametersBuilder, ParameterReader parameterReader) {
        declareVariableBuilder.append("${parameterReader.genericClass().clazz.simpleName} ${parameterReader.parameterName()} = new Object()\n")
        invokeBuilder.append("${parameterReader.parameterName()},")
        parametersBuilder.append("${parameterReader.genericClass().clazz.simpleName} ${parameterReader.parameterName()},")
        pathParametersBuilder.append("${parameterReader.parameterName()},")
    }

    //肯定是java.lang 中的类，不需要import
    protected void requestParameter(StringBuilder queryParameterBuilder, ParameterReader parameterReader) {
        queryParameterBuilder.append("${parameterReader.parameterName()}:")
        if (parameterReader.defaultValue() == null) {
            queryParameterBuilder.append("null,\n")
        } else {
            if (parameterReader.genericClass().clazz == String.class) {
                queryParameterBuilder.append("\"${parameterReader.defaultValue()}\",\n")
            } else {
                queryParameterBuilder.append("${parameterReader.defaultValue()},\n")
            }
        }
    }

    protected void requestBody(StringBuilder declareVariableBuilder, StringBuilder invokeBuilder, StringBuilder parametersBuilder, ParameterReader parameterReader) {
        ImportQueue.instance.add(parameterReader.genericClass().clazz.name)
        declareVariableBuilder.append("${parameterReader.genericClass().toString()}")

        parameterReader.genericClass().classList().each {
            ImportQueue.instance.add(it.name)
        }
        declareVariableBuilder.append(" ${parameterReader.parameterName()} ")

        if (parameterReader.genericClass().clazz.isInterface()) {
            declareVariableBuilder.append("= null\n")
        } else {
            declareVariableBuilder.append("= new ${parameterReader.genericClass().clazz.simpleName}")
            if (parameterReader.genericClass().genericList) {
                declareVariableBuilder.append("<>")
            }
            declareVariableBuilder.append("()\n")
        }

        invokeBuilder.append("${parameterReader.parameterName()} ,")
        parametersBuilder.append("${parameterReader.genericClass().toString()}")
        parametersBuilder.append(" ${parameterReader.parameterName()},")
    }

    protected void modelAttribute(StringBuilder formParameterBuilder, StringBuilder invokeBuilder, StringBuilder parametersBuilder, ParameterReader parameterReader) {
        formParameterBuilder.append("Map ${parameterReader.parameterName()} = [\n")
        parameterReader.fieldNames().each {
            formParameterBuilder.append("${it}:null,\n")
        }
        formParameterBuilder.append("]\n")
        invokeBuilder.append("${parameterReader.parameterName()},")
        parametersBuilder.append("Map ${parameterReader.parameterName()},")
    }
}
