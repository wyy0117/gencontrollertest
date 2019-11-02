package com.wyy.gencontrollertest.generator.method

import com.wyy.gencontrollertest.generator.IBodyGenerator

/**
 * @Date: 19-11-2
 * @Author: wyy
 */
class MethodGenerator implements IMethodGenerator {
    IBodyGenerator returnType(String returnType) {
        return null
    }

    IBodyGenerator name(String methodName) {
        return null
    }

    IBodyGenerator body(Object body) {
        return null
    }

    IBodyGenerator body(File file) {
        return null
    }

    IBodyGenerator url(String url) {
        return null
    }

    IBodyGenerator pathParamers(Object... pathParams) {
        return null
    }

    IBodyGenerator pathParamers(String path, Map<String, ?> pathParams) {
        return null
    }

    StringBuilder gen() {
        return null
    }
}
