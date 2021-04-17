package ${packageName}

<#list importClasses as class>
import ${class}
</#list>

import static io.restassured.RestAssured.*

/**
 * @Date: ${date}
 * @Author: ${author}
 */
class ${testClassname} {

    private String host = '${host}'
    private String context = '${context}'

    private final Gson gson = new GsonBuilder().setPrettyPrinting().create()

    <#if authType != 'NONE'>
    private String ${username} = 'username'//todo
    private String ${password} = 'password'//todo
    </#if>
<#if authType == "JWT">
    private String jwt

</#if>

<#if authType == "SESSION">
    private String session

</#if>
    @Before
    void before() {
        baseURI = host + context
<#if authType == "JWT">
        jwt = given()
        .body([username: username, password: password])
        .contentType(ContentType.JSON)
        .post('/login')//todo
        .then()
        .extract()
        .path('token')//todo
</#if>
<#if authType = "SESSION">
        RequestSpecification request = given()
                .config(config().encoderConfig(EncoderConfig.encoderConfig().defaultContentCharset(Charset.forName("UTF-8"))))
                .contentType(ContentType.JSON)
                .body([username: username, password: password])
        Validatable<ValidatableResponse, Response> response = request.post('test-post')
        session = response.cookies.JSESSIONID
</#if>
    }

<#list methods as method>
    @Test
    void ${method.name}Test() {
        <#if (method.bodyParameterList?size>0) >
            <#if method.bodyType == "List">
        List body = []
                <#elseif method.bodyType = "Map">
        Map body = [:]
                <#else>
        ${method.bodyType} body = new ${method.bodyType}(
                    <#list method.bodyParameterList as parameter>
                ${parameter.name}: <#if parameter.type == "String" && parameter.defaultValue != "null">'</#if>${parameter.defaultValue}<#if parameter.type == "String"&& parameter.defaultValue != "null">'</#if>,
                    </#list>
        )
            </#if>
        </#if>
    <#if (method.attributeParameterList?size>0) >
        Map attribute = [
        <#list method.attributeParameterList as parameter>
                ${parameter.name}: <#if parameter.type == "String" && parameter.defaultValue != "null">'</#if>${parameter.defaultValue}<#if parameter.type == "String"&& parameter.defaultValue != "null">'</#if>,
        </#list>
        ]
    </#if>
        <#if (method.pathParameterList?size>0) >
        Map pathParameters = [
        <#list method.pathParameterList as parameter>
                ${parameter.name}: <#if parameter.type == "String" && parameter.defaultValue != "null">'</#if>${parameter.defaultValue}<#if parameter.type == "String"&& parameter.defaultValue != "null">'</#if>,
        </#list>
        ]
        </#if>
        <#if (method.queryParameterList?size>0)>
        Map queryParameters = [
        <#list method.queryParameterList as parameter>
                ${parameter.name}: <#if parameter.type == "String" && parameter.defaultValue != "null">'</#if>${parameter.defaultValue}<#if parameter.type == "String"&& parameter.defaultValue != "null">'</#if>,
        </#list>
        ]
        </#if>
        <#if (method.fileParameter)?? >
        File ${method.fileParameter.name} = new File('')
        </#if>
        <#assign flag=false/>
        ${method.returnType} result = ${method.name}(<#if (method.bodyParameterList?size>0)>body<#assign flag=true></#if><#if (method.attributeParameterList?size>0)><#if flag>, </#if><#assign flag=true>attribute</#if><#if (method.pathParameterList?size>0)><#if flag>, </#if><#assign flag=true>pathParameters</#if><#if (method.queryParameterList?size>0)><#if flag>, </#if><#assign flag=true>queryParameters</#if><#if (method.fileParameter??)><#if flag>, </#if><#assign flag=true>${method.fileParameter.name}</#if>)
        println gson.toJson(result)
    }

    <#assign flag=false/>
    private ${method.returnType} ${method.name}(<#if (method.bodyParameterList?size>0)><#assign flag=true>${method.bodyType} body</#if><#if (method.attributeParameterList?size>0)><#if flag>, </#if><#assign flag=true>Map attribute</#if><#if (method.pathParameterList?size>0)><#if flag>, </#if><#assign flag=true>Map pathParameters</#if><#if (method.queryParameterList?size>0)><#if flag>, </#if><#assign flag=true>Map queryParameters</#if><#if (method.fileParameter??)><#if flag>, </#if><#assign flag=true>File ${method.fileParameter.name}</#if>) {
        RequestSpecification request = given()
                .config(config().encoderConfig(EncoderConfig.encoderConfig().defaultContentCharset(Charset.forName("${charset}"))))
    <#if authType == "JWT">
        .header('Authorization', jwt)
    </#if>
    <#if authType == "BASIC" >
        .auth().basic(username, password)
    </#if>
    <#if authType = "SESSION">
        request.cookie("JSSIONID", session)
    </#if>
     <#if (method.fileParameter)?? >
                .multiPart('file', file)
     </#if>
    <#if (method.attributeParameterList?size>0)>
        attribute.each {
            request.formParam(it.key, it.value)
        }
    </#if>
    <#if (method.bodyParameterList?size>0)>
                .contentType(ContentType.JSON)
                .body(body)
    </#if>
        <#if (method.queryParameterList?size>0)>
        queryParameters.each {
            request.queryParam(it.key, it.value)
        }
        </#if>
        <#if (method.pathParameterList?size>0)>
        pathParameters.each {
            request.pathParam(it.key, it.value)
        }
        </#if>
        Validatable<ValidatableResponse, Response> response = request.${method.requestMethod}('${method.uri}')
        response.then().statusCode(200)
    <#if method.returnType != "void">
        response.as(${method.responseType}.class)
    </#if>
    }

</#list>
}
