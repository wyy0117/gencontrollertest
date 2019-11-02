package com.wyy.gencontrollertest.generator.method

import com.wyy.gencontrollertest.generator.IBodyGenerator

/**
 * @Date: 19-11-2
 * @Author: wyy
 */
interface IMethodGenerator extends IBodyGenerator {

    IBodyGenerator returnType(String returnType)

    IBodyGenerator name(String methodName)

    IBodyGenerator body(Object body)

    IBodyGenerator body(File file)

    IBodyGenerator url(String url)

    IBodyGenerator pathParamers(Object... pathParams)

    IBodyGenerator pathParamers(String path, Map<String, ?> pathParams)

}
