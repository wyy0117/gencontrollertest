package com.wyy.gencontrollertest.generator.clazz

import com.wyy.gencontrollertest.config.GeneratorConfig
import com.wyy.gencontrollertest.generator.globlevariable.IGlobalVariableGenerator
import com.wyy.gencontrollertest.generator.method.IMethodGenerator

/**
 * @Date: 19-11-4
 * @Author: wyy
 */
abstract class ClassAbstractGenerator implements IClassGenerator {

    private GeneratorConfig config
    List<IMethodGenerator> methodGeneratorList = []
    IGlobalVariableGenerator globalVariableGenerator

    ClassAbstractGenerator(GeneratorConfig config, IGlobalVariableGenerator globalVariableGenerator, List<IMethodGenerator> methodGeneratorList) {
        this.config = config
        this.methodGeneratorList = methodGeneratorList
        this.globalVariableGenerator = globalVariableGenerator
    }

    @Override
    String className() {
        this.config.clazz.simpleName + "Test"
    }

    @Override
    StringBuilder gen() {
        StringBuilder builder = new StringBuilder()
        StringBuilder methodsBuilder = new StringBuilder()

        builder.append("class ${className()} {\n\n")
        methodGeneratorList.each {
            methodsBuilder.append(it.gen())
        }

        builder.append(globalVariableGenerator.gen())
        builder.append(methodsBuilder)
        builder.append("}\n")
        builder
    }
}
