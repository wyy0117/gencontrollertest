package com.wyy.gencontrollertest.generator

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.wyy.gencontrollertest.config.GeneratorConfig
import com.wyy.gencontrollertest.constant.ConfigConstant
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
import java.text.SimpleDateFormat

/**
 * @Date: 2020/11/25
 * @Author: wyy
 */
class TemplateDataGenerator {

    private GeneratorConfig config

    TemplateDataGenerator(GeneratorConfig config) {
        this.config = config
    }

    def gen() {
        Map templateData = [
                packageName  : config.packageName,
                clazz        : config.clazz,
                author       : config.getAuthor(),
                host         : config.host,
                context      : config.context,
                authType     : config.authType?.name(),
                after        : config.after,
                charset      : config.charset,
                testClassname: config.testClassname,
        ]

        templateData.date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())
        templateData.username = ConfigConstant.USERNAME
        templateData.password = ConfigConstant.PASSWORD


        if (config.after) {
            ImportQueue.instance.add(After.class.getName())
        }
        templateData
    }
}
