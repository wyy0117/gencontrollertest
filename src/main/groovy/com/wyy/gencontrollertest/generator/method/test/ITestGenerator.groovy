package com.wyy.gencontrollertest.generator.method.test

import com.wyy.gencontrollertest.generator.method.IMethodGenerator

/**
 * @Date: 19-11-5
 * @Author: wyy
 */
interface ITestGenerator extends IMethodGenerator {

    String url()

    String requestMethod()

}
