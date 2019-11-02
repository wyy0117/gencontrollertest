package com.wyy.test

import com.wyy.gencontrollertest.CodeGenerator
import com.wyy.test.controller.RestApiController
import org.junit.Test

/**
 * @Date: 19-11-2
 * @Author: wyy
 */
class CodeGeneratorTest {

    @Test
    void gen() {
        CodeGenerator generator = new CodeGenerator(RestApiController.class)
        generator.gen()
    }

}
