package com.lostAndFind.project.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Knife4j 接口文档配置
 *
 * https://doc.xiaominfo.com/knife4j/documentation/get_start.html
 *
 * @author yupi
 */
@Configuration
@EnableSwagger2
public class Knife4jConfiguration {

    @Bean(value = "defaultApi2")
    public Docket defaultApi2() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(new ApiInfoBuilder()
                        .title("lostAndFind")
                        .description("lostAndFind")
                        .termsOfServiceUrl("https://github.com/Djkyyds")
                        .contact(new Contact("djk", "https://github.com/Djkyyds", "2114275890@qq.com"))
                        .version("1.0")
                        .build())
                .groupName("2.X版本")
                .select()
                // 这里指定Controller扫描包路径
                .apis(RequestHandlerSelectors.basePackage("com.lostAndFind.project.controller"))
                .paths(PathSelectors.any())
                .build();
    }
    // 添加资源
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
        registry.addResourceHandler("doc.html").addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
    }
}