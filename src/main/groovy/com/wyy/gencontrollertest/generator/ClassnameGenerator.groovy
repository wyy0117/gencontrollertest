package com.wyy.gencontrollertest.generator

import com.wyy.gencontrollertest.config.GeneratorConfig
import com.wyy.gencontrollertest.reader.ClassReader

/**
 * @Date: 2020/11/25
 * @Author: wyy
 */
class ClassnameGenerator {

    private GeneratorConfig config

    ClassnameGenerator(GeneratorConfig config) {
        this.config = config
    }

    String gen() {
        ClassReader classReader = new ClassReader(config.clazz)
        String classname
        if ((classname = config.testClassname) == null) {
            classname = classReader.className() + "Test"
        }
        classname
    }
}
