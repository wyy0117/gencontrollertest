package com.wyy.gencontrollertest.generator.clazz

import com.wyy.gencontrollertest.config.GeneratorConfig
import com.wyy.gencontrollertest.generator.globlevariable.IGlobalVariableGenerator
import com.wyy.gencontrollertest.generator.method.IMethodGenerator

/**
 * @Date: 19-11-2
 * @Author: wyy
 */
class ClassGenerator extends ClassAbstractGenerator {


    ClassGenerator(GeneratorConfig config, IGlobalVariableGenerator globalVariableGenerator, List<IMethodGenerator> methodGeneratorList) {
        super(config, globalVariableGenerator, methodGeneratorList)
    }
}
