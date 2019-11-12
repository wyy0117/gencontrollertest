package com.wyy.test.gen

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.wyy.test.rest.dto.UserDTO
import io.restassured.http.ContentType
import io.restassured.specification.RequestSpecification
import org.junit.Before
import org.junit.Test

import static io.restassured.RestAssured.baseURI
import static io.restassured.RestAssured.given

/**
 * @Date: 2019-11-12 20:12:49
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
        println gson.toJson(request)
    }

    private String hello(queryMap) {
        RequestSpecification request = given()
                .header('Authorization', jwtToken)
        queryMap.each {
            request.queryParam(it.key, it.value)
        }
        request.get('hello')
                .as(String.class)
    }

    @Test
    void hello1Test() {
        long id = new Object()

        String result = hello1(id)
        println gson.toJson(request)
    }

    private String hello1(long id) {
        RequestSpecification request = given()
                .header('Authorization', jwtToken)
        request.delete('hello1', id)
                .as(String.class)
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
        request.post('hello2')
                .then()
                .statusCode(200)
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
        request.post('hello3')
                .then()
                .statusCode(200)
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
        request.post('hello4')
                .then()
                .statusCode(200)
    }

    @Test
    void hello5Test() {
        File[] files = []
        Map dto = [
                username: null,
                age     : null,
        ]

        List result = hello5(dto, files)
        println gson.toJson(request)
    }

    private List hello5(Map dto, File[] files) {
        RequestSpecification request = given()
                .header('Authorization', jwtToken)
        files.each {
            request.multiPart('files', it)
        }
        dto.each {
            request.formParam(it.key, it.value)
        }
        request.post('hello5')
                .as(List.class)
    }

}
