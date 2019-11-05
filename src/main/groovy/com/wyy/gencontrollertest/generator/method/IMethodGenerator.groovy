package com.wyy.gencontrollertest.generator.method

import com.wyy.gencontrollertest.generator.IGenerator


/**
 * @Date: 19-11-2
 * @Author: wyy
 */
interface IMethodGenerator extends IGenerator {

    Class returnType()

    String name()
}
