package com.wyy.gencontrollertest.generator.prefix

/**
 * @Date: 19-11-2
 * @Author: wyy
 */
class ImportQueue {

    private static List<String> importList = new ArrayList<>()

    static void add(String importClass) {
        importList.add(importClass)
    }

    static List<String> getImportList() {
        return importList
    }
}
