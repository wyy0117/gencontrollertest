package com.wyy.gencontrollertest.generator.globlevariable

/**
 * @Date: 19-11-4
 * @Author: wyy
 */
class GlobalVariableQueue {
    private static Set<String> queue = []

    static void add(String variable) {
        queue + variable
    }

    static Set<String> getQueue() {
        return queue
    }
}
