package com.wyy.gencontrollertest.generator.prefix

/**
 * @Date: 19-11-4
 * @Author: wyy
 */
@Singleton
class ImportQueue {
    private Set<String> queue = []
    private Set<String> ignore = [
            void.class.name, "java.lang", "java.util", byte.class.name, short.class.name, int.class.name, long.class.name, float.class.name, double.class.name, boolean.class.name, char.class.name

    ]

    void add(String... classNames) {
        classNames.each { className ->
            ignore.find({ className.startsWith(it) }) || queue.add(className)
        }
    }

    Set<String> getQueue() {
        queue
    }
}
