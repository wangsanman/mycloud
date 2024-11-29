package org.example.common;

import org.springframework.context.annotation.Bean;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;


//@Configuration // 标明是配置类
public class SwaggerConfig {
    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)  // DocumentationType.SWAGGER_2 固定的，代表swagger2
//                .groupName("分布式任务系统") // 如果配置多个文档的时候，那么需要配置groupName来分组标识
                .apiInfo(apiInfo()) // 用于生成API信息
                .select() // select()函数返回一个ApiSelectorBuilder实例,用来控制接口被swagger做成文档

//                .apis(Predicates.and(
//                        //Predicates.not(RequestHandlerSelectors.withMethodAnnotation(自定义注解.class), //方法有这个注解时,不生成这个方法的swagger文档
//                        RequestHandlerSelectors.basePackage("org.example.user.controller")
//                        )
//                )
                .apis(RequestHandlerSelectors.basePackage("org.example.user.controller")) // 用于指定扫描哪个包下的接口
                .paths(PathSelectors.any())// 选择所有的API,如果你想只为部分API生成文档，可以配置这里
//                .paths(PathSelectors.regex("/users/.*"))// 只给路径以users开头的请求显示文档 .*(.代表任意字符,*代表任意个数)
//                .paths(Predicates.not(PathSelectors.regex("/users/.*")))  //不生成该接口文档
                .build();
    }

    /**
     * 用于定义API主界面的信息，比如可以声明所有的API的总标题、描述、版本
     *
     * @return
     */
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("用户项目swagger文档") //文档真正的标题
                .description("用户项目swagger文档描述") // 文档详细描述
                .version("1.1") // 可以用来定义版本。

                .contact(new Contact(
                        "wangyiman博客",//网站发布者企业名称
                        "wangyiman.boke.com",//网站发布者企业官网
                        "13716759140@163.com")//网站发布企业邮箱
                )
                .build(); //
    }
}
