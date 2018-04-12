package com.kotei.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * 总启动项。命令行启动入口
 */
//@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@EnableSwagger2
//@MapperScan("com.kotei.web.dao")
@ComponentScan(basePackages =
        {
                "com.kotei.satellitegraph",
                "com.kotei.allweather",
                "com.kotei.core",
                "com.kotei.illegalclue",
                "com.kotei.satellitegraph",
                "com.kotei.web",
                "com.kotei.config",
                "com.kotei.workflowengine"
        })
@SpringBootApplication
//@SpringBootApplication(exclude={DataSourceAutoConfiguration.class,HibernateJpaAutoConfiguration.class})
public class WebApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebApplication.class, args);
    }


}
