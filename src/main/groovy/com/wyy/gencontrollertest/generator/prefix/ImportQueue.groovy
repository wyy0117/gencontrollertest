package com.wyy.gencontrollertest.generator.prefix

/**
 * @Date: 19-11-4
 * @Author: wyy
 */
@Singleton
class ImportQueue {
    private Set<String> queue = []
    private Set<String> ignore = [
            "void",
            "java.lang",
            "java.util"
    ]

    void add(String className) {
        ignore.find({ className.startsWith(it) }) || queue.add(className)
    }

    Set<String> getQueue() {
        return queue
    }
}
