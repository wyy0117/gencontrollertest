package com.wyy.gencontrollertest.generator.method.before

import com.wyy.gencontrollertest.config.AuthType
import com.wyy.gencontrollertest.config.ConfigConstant
import com.wyy.gencontrollertest.config.GeneratorConfig
import com.wyy.gencontrollertest.generator.prefix.ImportQueue
import com.wyy.gencontrollertest.reader.GenericClass
import io.restassured.http.ContentType
import org.junit.Before

/**
 * @Date: 19-11-2
 * @Author: wyy
 */
abstract class BeforeAbstractGenerator implements IBeforeGenerator {

    private GeneratorConfig config

    BeforeAbstractGenerator(GeneratorConfig config) {
        this.config = config
    }

    @Override
    GenericClass returnType() {
        new GenericClass(void.class)
    }

    @Override
    String name() {
        "before"
    }

    StringBuilder gen() {
        StringBuilder builder = new StringBuilder()
        ImportQueue.instance.add(Before.class.name)
        builder.append("@Before\n")
        builder.append("${returnType().clazz.simpleName} ${name()}(){\n")
        builder.append("baseURI = ${ConfigConstant.HOST} + ${ConfigConstant.CONTEXT}\n")

        if (config.authType == AuthType.JWT) {
            println "++++++you are using jwt auth,please update username,password and login url in the before method++++++"
            builder.append("${ConfigConstant.JWT_TOKEN} = given()\n")
            builder.append(".body([username: ${ConfigConstant.USERNAME}, password: ${ConfigConstant.PASSWORD}])\n")
            ImportQueue.instance.add(ContentType.class.name)
            builder.append(".contentType(ContentType.JSON)\n")
            builder.append(".post('/login')//todo \n")
            builder.append(".then()\n")
            builder.append(".extract()\n")
            builder.append(".path('token')//todo\n")
        }
        builder.append("}\n\n")
        builder
    }
}
