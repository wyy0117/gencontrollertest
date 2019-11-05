package com.wyy.gencontrollertest.generator.prefix

import com.wyy.gencontrollertest.config.GeneratorConfig
import io.restassured.RestAssured

import java.text.SimpleDateFormat

/**
 * @Date: 19-11-4
 * @Author: wyy
 */
abstract class PrefixAbstractGenerator implements IPrefixGenerator {

    private GeneratorConfig config
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

    PrefixAbstractGenerator(GeneratorConfig config) {
        this.config = config
    }

    @Override
    String packageName() {
        config.packageName
    }

    @Override
    String author() {
        config.author
    }

    @Override
    StringBuilder gen() {
        StringBuilder stringBuilder = new StringBuilder()
        stringBuilder.append("package ${packageName()}\n")
        stringBuilder.append("\n")

        ImportQueue.instance.queue.each {
            stringBuilder.append("import $it\n")
        }
        stringBuilder.append("import static ${RestAssured.class.name}.*\n")
        stringBuilder.append("\n")
        stringBuilder.append("/**\n")
        stringBuilder.append("* @Date: ${simpleDateFormat.format(new Date())}\n")
        stringBuilder.append("* @Author: ${author()}\n")
        stringBuilder.append("*/\n\n")

        stringBuilder
    }
}
