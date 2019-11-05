package com.wyy.gencontrollertest.writer

import com.wyy.gencontrollertest.config.GeneratorConfig

/**
 * @Date: 19-11-4
 * @Author: wyy
 */
class CodeFileWriter {
    private GeneratorConfig config

    CodeFileWriter(GeneratorConfig config) {
        this.config = config
    }

    void write(String context) {
        String path = System.getProperty("user.dir") + "/src/test/groovy"

        File folder = new File(path, config.packageName.replace(".", "/"))
        if (!folder.exists()) {
            folder.mkdirs()
        }
        File file = new File(folder, config.aClass.simpleName + "Test.groovy")
        println "{file.absolutePath} = ${file.absolutePath}"
        file.text = context
    }
}
