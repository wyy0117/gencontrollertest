package com.wyy.gencontrollertest.config

/**
 * @Date: 19-11-4
 * @Author: wyy
 */
class GeneratorConfig {

    /**
     * 生成的代码的包路径，决定了生成代码的路径信息，默认使用controller的包路径
     */
    String packageName
    /**
     * 要生产测试代码的controller类
     */
    Class<?> aClass
    /**
     * 作者信息
     */
    String author
    String host = 'http://localhost:8080'
    /**
     * rest接口的application context
     */
    String context = ""
    AuthType authType
    /**
     * 是否生产before
     */
    boolean before = false
    /**
     * 是否生产after
     */
    boolean after = false

    /**
     * 单元测试类的名字
     */
    String testClassName

    String getPackageName() {
        packageName ?: aClass.package.name
    }

    String getAuthor() {
        author ?: System.getProperty("user.name")
    }

    String getContext() {
        if (context.length() == 0) {
            return context
        }
        context.startsWith("/") || (context = "/" + context)
        context
    }
}
