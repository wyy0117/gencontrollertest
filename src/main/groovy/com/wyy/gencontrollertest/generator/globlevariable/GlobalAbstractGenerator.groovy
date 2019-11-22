package com.wyy.gencontrollertest.generator.globlevariable

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.wyy.gencontrollertest.config.AuthType
import com.wyy.gencontrollertest.config.ConfigConstant
import com.wyy.gencontrollertest.config.GeneratorConfig
import com.wyy.gencontrollertest.generator.prefix.ImportQueue
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
        config.context + new ClassReader(config.clazz).url()
    }

    @Override
    StringBuilder gen() {
        StringBuilder builder = new StringBuilder()
        builder.append("private String ${ConfigConstant.HOST} = '${host()}'\n")
        builder.append("private String ${ConfigConstant.CONTEXT} = '${context()}'\n")
        ImportQueue.instance.add(Gson.class.name)
        ImportQueue.instance.add(GsonBuilder.class.name)
        builder.append("private final Gson gson = new GsonBuilder().setPrettyPrinting().create()\n")

        if (config.authType != null) {
            builder.append("private String ${ConfigConstant.USERNAME} = 'username'//todo\n")
            builder.append("private String ${ConfigConstant.PASSWORD} = 'password'//todo\n")
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
