package com.wyy.gencontrollertest.generator.method.after

import com.wyy.gencontrollertest.config.GeneratorConfig
import com.wyy.gencontrollertest.generator.prefix.ImportQueue
import com.wyy.gencontrollertest.reader.GenericClass
import org.junit.After

/**
 * @Date: 19-11-5
 * @Author: wyy
 */
abstract class AfterAbstractGenerator implements IAfterGenerator {
    private GeneratorConfig config

    AfterAbstractGenerator(GeneratorConfig config) {
        this.config = config
    }

    @Override
    GenericClass returnType() {
        new GenericClass(void.class)
    }

    @Override
    String name() {
        "after"
    }

    @Override
    StringBuilder gen() {
        ImportQueue.instance.add(After.class.name)
        StringBuilder builder = new StringBuilder()
        builder.append("@After\n")
        builder.append("${returnType().clazz.simpleName} ${name()} (){\n\n")
        builder.append("}\n\n")

        builder
    }
}
