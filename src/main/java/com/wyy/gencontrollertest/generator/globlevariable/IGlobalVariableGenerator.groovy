package com.wyy.gencontrollertest.generator.globlevariable

import com.wyy.gencontrollertest.generator.IBodyGenerator

/**
 * @Date: 19-11-2
 * @Author: wyy
 */
interface IGlobalVariableGenerator extends IBodyGenerator {

    IGlobalVariableGenerator host(String host)

    IGlobalVariableGenerator context(String context)
}
