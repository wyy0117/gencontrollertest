package com.wyy.gencontrollertest

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.wyy.gencontrollertest.config.GeneratorConfig
import com.wyy.gencontrollertest.constant.ConfigConstant
import com.wyy.gencontrollertest.generator.ClassnameGenerator
import com.wyy.gencontrollertest.generator.ImportQueue
import com.wyy.gencontrollertest.generator.MethodGenerator
import com.wyy.gencontrollertest.generator.TemplateDataGenerator
import com.wyy.gencontrollertest.reader.*
import freemarker.template.Configuration
import freemarker.template.Template
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
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.multipart.MultipartFile

import java.lang.reflect.Field
import java.lang.reflect.Parameter
import java.nio.charset.Charset
import java.text.SimpleDateFormat

/**
 * @Date: 19-11-2
 * @Author: wyy
 */
class CodeGenerator {

    private GeneratorConfig config
    private final String templateFileName = 'RestApiControllerTest.ftl'

    CodeGenerator(GeneratorConfig config) {
        this.config = config
    }

    void gen() {
        // step1 创建freeMarker配置实例
        Configuration configuration = new Configuration();
        // step2 获取模版路径
        configuration.setClassForTemplateLoading(CodeGenerator.class, '/templates')
        // step3 创建数据模型
        Map templateData = new TemplateDataGenerator(config).gen()
        templateData.testClassname = new ClassnameGenerator(config).gen()

        templateData.methods = new MethodGenerator(config).gen()
        templateData.importClasses = ImportQueue.instance.queue
        // step4 加载模版文件
        Template template = configuration.getTemplate(templateFileName);
        // step5 生成数据
        String path = System.getProperty("user.dir") + "/src/test/groovy"

        File folder = new File(path, config.packageName.replace(".", "/"))
        if (!folder.exists()) {
            folder.mkdirs()
        }

        String fileName = templateData.testClassname + ".groovy"

        folder.listFiles()
        int size = folder.listFiles({ file, name ->
            return name.startsWith(fileName)
        } as FilenameFilter).size()
        size > 0 && (fileName += size)
        File file = new File(folder, fileName)
        Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)));
        // step6 输出文件
        template.process(templateData, out);
    }
}
