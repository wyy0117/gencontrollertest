package com.wyy.gencontrollertest.generator.globlevariable

import com.wyy.gencontrollertest.config.AuthType
import com.wyy.gencontrollertest.config.ConfigConstant
import com.wyy.gencontrollertest.config.GeneratorConfig
import com.wyy.gencontrollertest.reader.ClassReader

/**
 * @Date: 19-11-4
 * @Author: wyy
 */
abstract class GlobalAbstractGenerator implements IGlobalVariableGenerator {

    private GeneratorConfig config

    GlobalAbstractGenerator(GeneratorConfig config) {
        this.config = config
    }

    @Override
    String host() {
        config.host
    }

    @Override
    String context() {
        config.context + new ClassReader(config.aClass).url()
    }

    @Override
    StringBuilder gen() {
        StringBuilder builder = new StringBuilder()
        builder.append("private String ${ConfigConstant.HOST} = '${host()}'\n")
        builder.append("private String ${ConfigConstant.CONTEXT} = '${context()}'\n")

        if (config.authType != null) {
            builder.append("private String ${ConfigConstant.USERNAME} = 'username'\n")
            builder.append("private String ${ConfigConstant.PASSWORD} = 'password'\n")
        }

        if (config.authType == AuthType.JWT) {

            builder.append("private String ${ConfigConstant.JWT_TOKEN}\n")
        }
        GlobalVariableQueue.queue.each {
            builder.append(it)
        }
        builder
    }
}
