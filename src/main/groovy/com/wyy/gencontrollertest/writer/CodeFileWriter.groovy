package com.wyy.gencontrollertest.writer

import com.wyy.gencontrollertest.config.GeneratorConfig
import com.wyy.gencontrollertest.reader.ClassReader

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

        String fileName = (config.testClassName ?: new ClassReader(config.aClass).className() + "Test") + ".groovy"

        folder.listFiles()
        int size = folder.listFiles({ file, name ->
            return name.startsWith(fileName)
        } as FilenameFilter).size()
        size > 0 && (fileName += size)
        File file = new File(folder, fileName)
        println "generated file absolutePath : ${file.absolutePath}"
        file.text = context
    }
}
