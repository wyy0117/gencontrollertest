package com.wyy.test

import com.wyy.gencontrollertest.CodeGenerator
import com.wyy.gencontrollertest.config.AuthType
import com.wyy.gencontrollertest.config.GeneratorConfig
import com.wyy.test.rest.controller.RestApiController
import org.junit.Test

/**
 * @Date: 19-11-2
 * @Author: wyy
 */
class CodeGeneratorTest {

    @Test
    void gen() {

        new CodeGenerator(new GeneratorConfig(aClass: RestApiController.class, packageName: 'com.wyy.test.gen', context: "gen", authType: AuthType.JWT, before: true)).gen()
    }

}
