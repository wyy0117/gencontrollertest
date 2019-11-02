package com.wyy.gencontrollertest.generator.clazz

import com.wyy.gencontrollertest.generator.IBodyGenerator
import com.wyy.gencontrollertest.generator.IGenerator

/**
 * @Date: 19-11-2
 * @Author: wyy
 */
class ClassGenerator implements IClassGenerator {

    IGenerator className(String className) {
        return this
    }

    IGenerator body(IBodyGenerator... bodyGenerators) {
        return this
    }

    StringBuilder gen() {
        return null
    }
}
