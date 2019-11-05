package com.wyy.gencontrollertest.generator.method.test

import com.wyy.gencontrollertest.config.GeneratorConfig

import java.lang.reflect.Method

/**
 * @Date: 19-11-2
 * @Author: wyy
 */
class TestGenerator extends TestAbstractGenerator {


    TestGenerator(Method method, GeneratorConfig config) {
        super(method, config)
    }
}
