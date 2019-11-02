package com.wyy.gencontrollertest.generator.clazz

import com.wyy.gencontrollertest.generator.IBodyGenerator
import com.wyy.gencontrollertest.generator.IGenerator

/**
 * @Date: 19-11-2
 * @Author: wyy
 */
interface IClassGenerator extends IGenerator {


    IGenerator className(String className)

    IGenerator body(IBodyGenerator... bodyGenerators)
}
