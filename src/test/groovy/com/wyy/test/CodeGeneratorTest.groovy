package com.wyy.test


import com.wyy.gencontrollertest.CodeGenerator
import com.wyy.gencontrollertest.constant.AuthType
import com.wyy.gencontrollertest.config.GeneratorConfig
import com.wyy.test.rest.controller.RestApiController
import org.junit.Test

/**
 * @Date: 19-11-2
 * @Author: wyy
 */
class CodeGeneratorTest {


    @Test
    void testftl() {
        new CodeGenerator(new GeneratorConfig(clazz: RestApiController.class, packageName: 'com.wyy.test.gen', context: "gen", authType: AuthType.SESSION,  charset: "UTF-8")).gen()

    }

}
