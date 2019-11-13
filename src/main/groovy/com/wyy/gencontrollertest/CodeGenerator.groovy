package com.wyy.gencontrollertest

import com.wyy.gencontrollertest.config.GeneratorConfig
import com.wyy.gencontrollertest.generator.clazz.ClassGenerator
import com.wyy.gencontrollertest.generator.globlevariable.GlobalVariableGenerator
import com.wyy.gencontrollertest.generator.method.IMethodGenerator
import com.wyy.gencontrollertest.generator.method.after.AfterGenerator
import com.wyy.gencontrollertest.generator.method.after.IAfterGenerator
import com.wyy.gencontrollertest.generator.method.before.BeforeGenerator
import com.wyy.gencontrollertest.generator.method.before.IBeforeGenerator
import com.wyy.gencontrollertest.generator.method.test.TestGenerator
import com.wyy.gencontrollertest.generator.prefix.PrefixGenerator
import com.wyy.gencontrollertest.reader.ClassReader
import com.wyy.gencontrollertest.writer.CodeFileWriter

/**
 * @Date: 19-11-2
 * @Author: wyy
 */
class CodeGenerator {

    private GeneratorConfig config
    private IBeforeGenerator beforeGenerator
    private IAfterGenerator afterGenerator

    CodeGenerator(Class<?> aClass) {
        config = new GeneratorConfig(aClass: aClass)
    }

    CodeGenerator(GeneratorConfig config) {
        this.config = config
    }

    void setBeforeGenerator(IBeforeGenerator beforeGenerator) {
        this.beforeGenerator = beforeGenerator
    }

    void setAfterGenerator(IAfterGenerator afterGenerator) {
        this.afterGenerator = afterGenerator
    }

    void gen() {
        StringBuilder stringBuilder = new StringBuilder()

        ClassReader classReader = new ClassReader(config.aClass)
        List<IMethodGenerator> methodGeneratorList = []
        if (config.before) {
            if (beforeGenerator != null) {
                methodGeneratorList.add(beforeGenerator)
            } else {
                methodGeneratorList.add(new BeforeGenerator(config))
            }
        }
        if (config.after) {
            if (afterGenerator != null) {
                methodGeneratorList.add(afterGenerator)
            } else {
                methodGeneratorList.add(new AfterGenerator(config))
            }
        }

        methodGeneratorList.addAll(classReader.methods().collect({ new TestGenerator(it, config) }))

        StringBuilder classBuilder = new ClassGenerator(config, new GlobalVariableGenerator(config), methodGeneratorList).gen()
        StringBuilder prefixBuilder = new PrefixGenerator(config).gen()
        stringBuilder.append(prefixBuilder)
        stringBuilder.append(classBuilder)
        new CodeFileWriter(config).write(stringBuilder.toString())
    }
}
