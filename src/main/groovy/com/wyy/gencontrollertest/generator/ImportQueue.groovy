package com.wyy.gencontrollertest.generator

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import io.restassured.RestAssured
import io.restassured.config.EncoderConfig
import io.restassured.http.ContentType
import io.restassured.response.Response
import io.restassured.response.Validatable
import io.restassured.response.ValidatableResponse
import io.restassured.specification.RequestSpecification
import org.junit.After
import org.junit.Before
import org.junit.Test

import java.nio.charset.Charset

/**
 * @Date: 19-11-4
 * @Author: wyy
 */
class ImportQueue {
    private Set<String> queue = []
    private Set<String> ignore = [
            void.class.name, "java.lang", "java.util", byte.class.name, short.class.name, int.class.name, long.class.name, float.class.name, double.class.name, boolean.class.name, char.class.name
    ]

    private static ImportQueue importQueue = null

    private ImportQueue() {
    }

    static ImportQueue getInstance() {
        if (importQueue == null) {
            synchronized (ImportQueue.class) {
                if (importQueue == null) {
                    importQueue = new ImportQueue()
                    importQueue.add(Gson.class.name, GsonBuilder.class.name, Test.class.name, Before.class.name, After.class.name,
                            RequestSpecification.class.name, Validatable.class.name, ValidatableResponse.class.name, Response.class.name,
                            RestAssured.class.name, EncoderConfig.class.name, Charset.class.name, ContentType.class.name)
                }
            }
        }
        return importQueue
    }

    void add(String... classNames) {
        classNames.each { className ->
            ignore.find({ className.startsWith(it) }) || queue.add(className)
        }
    }

    Set<String> getQueue() {
        queue
    }
}
