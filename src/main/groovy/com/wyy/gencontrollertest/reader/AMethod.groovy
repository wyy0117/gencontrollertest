package com.wyy.gencontrollertest.reader

/**
 * @Date: 2020/11/24
 * @Author: wyy
 */
class AMethod {

    String name
    String uri
    String returnType
    String requestMethod
    String responseType

    List<AParameter> bodyParameterList = []
    List<AParameter> attributeParameterList = []
    List<AParameter> pathParameterList = []
    List<AParameter> queryParameterList = []
    String bodyType

    AParameter fileParameter
}
