package com.wyy.test.gen

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.wyy.test.rest.dto.UserDTO
import io.restassured.http.ContentType
import io.restassured.response.Response
import io.restassured.response.Validatable
import io.restassured.response.ValidatableResponse
import io.restassured.specification.RequestSpecification
import org.junit.Before
import org.junit.Test

import static io.restassured.RestAssured.baseURI
import static io.restassured.RestAssured.given

/**
 * @Date: 2020-01-14 10:53:38
 * @Author: wyy
 */

class RestApiControllerTest {

    private String host = 'http://localhost:8080'
    private String context = '/gen'
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create()
    private String username = 'username'//todo
    private String password = 'password'//todo
    private String jwtToken

    @Before
    void before() {
        baseURI = host + context
        jwtToken = given()
                .body([username: username, password: password])
                .contentType(ContentType.JSON)
                .post('/login')//todo
                .then()
                .extract()
                .path('token')//todo
    }

    @Test
    void helloTest() {
        Map queryMap = [
                a: "abc",
                b: null,
                c: 1,
        ]

        String result = hello(queryMap)
        println gson.toJson(result)
    }

    private String hello(queryMap) {
        RequestSpecification request = given()
                .header('Authorization', jwtToken)
        queryMap.each {
            request.queryParam(it.key, it.value)
        }
        Validatable<ValidatableResponse, Response> response = request.get('hello')
        response.then().statusCode(200)
        response.as(String.class)
    }

    @Test
    void hello1Test() {
        long id = new Object()

        String result = hello1(id)
        println gson.toJson(result)
    }

    private String hello1(long id) {
        RequestSpecification request = given()
                .header('Authorization', jwtToken)
        Validatable<ValidatableResponse, Response> response = request.delete('hello1', id)
        response.then().statusCode(200)
        response.as(String.class)
    }

    @Test
    void hello2Test() {
        UserDTO dto = new UserDTO()

        hello2(dto)
    }

    private void hello2(UserDTO dto) {
        RequestSpecification request = given()
                .header('Authorization', jwtToken)
                .contentType(ContentType.JSON)
                .body(dto)
        Validatable<ValidatableResponse, Response> response = request.post('hello2')
        response.then().statusCode(200)
    }

    @Test
    void hello3Test() {
        File file = new File('')
        Map dto = [
                username: null,
                age     : null,
        ]

        hello3(dto, file)
    }

    private void hello3(Map dto, File file) {
        RequestSpecification request = given()
                .header('Authorization', jwtToken)
                .multiPart('file', file)
        dto.each {
            request.formParam(it.key, it.value)
        }
        Validatable<ValidatableResponse, Response> response = request.post('hello3')
        response.then().statusCode(200)
    }

    @Test
    void hello4Test() {
        List<UserDTO> dto = null

        hello4(dto)
    }

    private void hello4(List<UserDTO> dto) {
        RequestSpecification request = given()
                .header('Authorization', jwtToken)
                .contentType(ContentType.JSON)
                .body(dto)
        Validatable<ValidatableResponse, Response> response = request.post('hello4')
        response.then().statusCode(200)
    }

    @Test
    void hello5Test() {
        File[] files = []
        Map dto = [
                username: null,
                age     : null,
        ]

        List<UserDTO> result = hello5(dto, files)
        println gson.toJson(result)
    }

    private List<UserDTO> hello5(Map dto, File[] files) {
        RequestSpecification request = given()
                .header('Authorization', jwtToken)
        files.each {
            request.multiPart('files', it)
        }
        dto.each {
            request.formParam(it.key, it.value)
        }
        Validatable<ValidatableResponse, Response> response = request.post('hello5')
        response.then().statusCode(200)
        response.as(List.class)
    }

    @Test
    void hello6Test() {

        Map<String, List<UserDTO>> result = hello6()
        println gson.toJson(result)
    }

    private Map<String, List<UserDTO>> hello6() {
        RequestSpecification request = given()
                .header('Authorization', jwtToken)
        Validatable<ValidatableResponse, Response> response = request.get('hello6')
        response.then().statusCode(200)
        response.as(Map.class)
    }

    @Test
    void hello7Test() {
        Map queryMap = [
                a: null,
                b: null,
        ]

        long result = hello7(queryMap)
        println gson.toJson(result)
    }

    private long hello7(queryMap) {
        RequestSpecification request = given()
                .header('Authorization', jwtToken)
        queryMap.each {
            request.queryParam(it.key, it.value)
        }
        Validatable<ValidatableResponse, Response> response = request.get('hello7')
        response.then().statusCode(200)
        response.as(long.class)
    }
}
