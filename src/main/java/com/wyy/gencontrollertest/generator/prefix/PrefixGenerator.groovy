package com.wyy.gencontrollertest.generator.prefix

/**
 * @Date: 19-11-2
 * @Author: wyy
 */
class PrefixGenerator implements IPrefixGenerator {

    private String packageName

    StringBuilder gen() {
        return null
    }

    IPrefixGenerator packageName(String packageName) {
        this.packageName = packageName
        return this
    }
}
