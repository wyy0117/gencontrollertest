### 项目简介
该项目是为了给写的controller跑单元测试而开发的，目的只有一个，测试接口更加快速便捷。开发语言及生产的代码使用groovy，根据编写的controller类生成对应的测试代码，生成的代码基于第三方测试框架[rest-assured](https://github.com/rest-assured/rest-assured)，基本可以满足所有的GET,POST,DELETE,PUT请求，通过代码发送rest请求调用后台接口，模拟真实调用接口场景。    
### 为什么写这个项目
Q: 现在的大多数项目都是基于springboot的，springboot提供了非常便捷的测试方式，可以直接把service注入到测试代码中，非常方便调试。对于非springboot的项目，也可以使用一下几种方式测试：swagger，postman，curl等等，为什么还要写这么一个项目那？  
A: springboot项目的测试，每跑一个单元测试，基本是需要项目启动起来的，跑完测试，项目立刻停止，跑第二个测试又需要启一次项目，虽然springboot项目启动非常块，但是整个测试下来10s是需要的，整个项目的开发过程中不知道要花费掉多少个10s。如果是把项目先启动起来，然后使用swagger或者postman等测试，这样可以节省一些启动项目的时间，但是测试时的参数是无法得到保留的，今天写一遍参数，明天可能还要再写一遍参数，就算打字速度6的起飞也快不过springboot的测试速度。很多情况下跑测试是因为前端同学告诉你接口报错了那么你就要模拟前端调用接口的方式去测试自己写的接口，此时你是否还能记起每个参数到底什么意思？参数应该怎么填？  
Q：为什么使用groovy而不是java？  
A：对于单元测试来说，速度，性能等都是无关紧要的，最终目的是为了把接口测试一遍，参数怎么方便怎么拼，代码行数怎么少就怎么写。java这种强类型语言不太适合，groovy这种半强类型就非常灵活，需要强类型就可以强类型，不需要就可以像js那样灵活，而且跟java又非常像，学习成本相对较低。  
### 使用方法
1. 使用最新代码
    1. clone项目
        ```
        git clone https://github.com/wyy0117/gencontrollertest.git
        ```
    1. intall
        ```
        mvn install
        ```    
    1. 在需要使用的项目中添加依赖
        ```
        <dependency>
            <groupId>com.github.wyy0117</groupId>
            <artifactId>gencontrollertest</artifactId>
            <version>1.1</version>
            <scope>test</scope>
        </dependency>
        ```    
1. 直接使用maven中央仓库
    ```
    <dependency>
        <groupId>com.github.wyy0117</groupId>
        <artifactId>gencontrollertest</artifactId>
        <version>1.1</version>
        <scope>test</scope>
    </dependency>
    ```            
1. 为了反射能读取到方法的参数的名字，生成代码时需要添加编译参数‘-parameters’，代码生成后该参数即可删除。
    ##### maven
    ```
    <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-compiler-plugin</artifactId>
        <version>3.3</version>
        <configuration>
            <source>1.8</source>
            <target>1.8</target>
            <compilerArgs>
                <arg>-parameters</arg>
            </compilerArgs>
        </configuration>
    </plugin>
    ```
    ##### gradle
    ```
    compileTestJava {
        options.compilerArgs << '-parameters'
    }
    ```
1. 如果重复生成同一个controller的代码，文件会以后缀添加递增数字的方式生成而不会覆盖已生产的文件，生成的测试代码的默认类名为原controller的类名+“Test”.
1. 使用方法
    ```
    @Test
    void gen() {
        new CodeGenerator(new GeneratorConfig(clazz: RestApiController.class, packageName: 'com.wyy.test.gen', context: "gen",authType: AuthType.JWT)).gen()
    }
    ```  
1. 配置参数说明  
<table>
    <tr>
        <td>参数</td>
        <td>默认值</td>
        <td>解释</td>
    </tr>
   <tr>
      <td>clazz</td>
      <td></td>
      <td>要生产测试代码的controller类</td>
   </tr>
   <tr>
      <td>packageName</td>
      <td>默认使用controller的包路径</td>
      <td>生成的代码的包路径，决定了生成代码的路径信息</td>
   </tr>
   <tr>
      <td>author</td>
      <td>系统当前用户的用户名</td>
      <td>作者信息</td>
   </tr>
   <tr>
      <td>host</td>
      <td>http://localhost:8080</td>
      <td>服务器的域名</td>
   </tr>
   <tr>
      <td>context</td>
      <td></td>
      <td>接口的application context</td>
   </tr>
   <tr>
      <td>authType</td>
      <td></td>
      <td>认证类型</td>
   </tr>
   <tr>
      <td>before</td>
      <td>false</td>
      <td>是否生产before</td>
   </tr>
   <tr>
      <td>after</td>
      <td>false</td>
      <td>是否生产after</td>
   </tr>
   <tr>
      <td>testClassName</td>
      <td>controller的类名+Test</td>
      <td>单元测试类的名字</td>
   </tr>
</table>

#### TODO
1. ~~代码格式化~~  
    因为现在的编辑器都有格式化代码的功能，所以生成的代码没有格式化问题也不大。  
1. ~~参数中使用了泛型类型，生成的代码中也使用泛型类型~~
1. ~~使用form表单请求时，定义好都需要填哪些字段~~  
    build模式下反射不到属性的set方法，暂时使用所有的属性。
1. ~~参数的默认值~~
1. ~~返回类型包含泛型~~
1. ~~基本对象不要在打印时转为json~~  
    经测试，基本类型转json不会报错，输出信息正常。
1. ~~嵌套泛型类型~~v1.1  
1. ~~java基础类型不需要import~~v1.2
1. ~~优先处理文件~~v1.2.1
