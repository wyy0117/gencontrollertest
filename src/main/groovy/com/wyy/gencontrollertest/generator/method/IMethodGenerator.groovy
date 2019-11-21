package com.wyy.gencontrollertest.generator.method

import com.wyy.gencontrollertest.generator.IGenerator
import com.wyy.gencontrollertest.reader.GenericClass


/**
 * @Date: 19-11-2
 * @Author: wyy
 */
interface IMethodGenerator extends IGenerator {

    GenericClass returnType()

    String name()
}
