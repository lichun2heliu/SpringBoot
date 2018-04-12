package com.kotei.web;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;
//import springfox.documentation.builders.ApiInfoBuilder;
//import springfox.documentation.builders.ParameterBuilder;
//import springfox.documentation.builders.PathSelectors;
//import springfox.documentation.builders.RequestHandlerSelectors;
//import springfox.documentation.schema.ModelRef;
//import springfox.documentation.service.ApiInfo;
//import springfox.documentation.service.Parameter;
//import springfox.documentation.spi.DocumentationType;
//import springfox.documentation.spring.web.plugins.Docket;
//import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

/**
 * 项目名称：phalaenopsis
 * 创建日期：2018/3/1
 * 修改历史：
 * 1. [2018/3/1]创建文件
 *
 * @author chunl
 */
@Configurable
//@EnableSwagger2
public class Swagger2 {


//    @Bean
//    public Docket createRestApiAllWeather() {
//
//        ParameterBuilder tokenPer = new ParameterBuilder();
//        List<Parameter> pars = new ArrayList<Parameter>();
//        tokenPer.name("AUTH_ID").description("ticket 令牌")
//                .modelRef(new ModelRef("string")).parameterType("header").required(true).build();
//        pars.add(tokenPer.build());
//
//        return new Docket(DocumentationType.SWAGGER_2)
//                .apiInfo(apiInfo())
//                .select()
//                .apis(RequestHandlerSelectors.basePackage("com.kotei.web.controller"))
//                .paths(PathSelectors.any())
//                .build()
//                .globalOperationParameters(pars);
//    }
//
//
//    private ApiInfo apiInfo() {
//        return new ApiInfoBuilder()
////                .title("Spring MVC中使用Swagger2构建RESTful APIs")
////                .description("使用mongodb构建api接口")
////                .termsOfServiceUrl("https://www.kotei.info/")
////                .contact(new Contact("chunl","","chunl@kotei-info.com"))
////                .version("1.0")
////                .build();
//                .title("基础平台 RESTful APIs")
//                .description("基础平台 RESTful 风格的接口文档，内容详细，极大的减少了前后端的沟通成本，同时确保代码与文档保持高度一致，极大的减少维护文档的时间。")
//                .termsOfServiceUrl("https://www.kotei.info/")
//                .contact("chunl")
//                .version("1.0.0")
//                .build();
//    }
}
