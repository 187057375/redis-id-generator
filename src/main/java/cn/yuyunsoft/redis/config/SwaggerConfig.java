package cn.yuyunsoft.redis.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 *
 * @功能: swagger配置
 * @作者: 黄小云
 * @版本: v1.0.0
 * @时间: 2016年12月10日
 * @描述: 配置并启用swagger
 *
 */

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Value("${swagger.ui.enable}")
    private boolean environmentSpecificBooleanFlag;

    @Bean
    public Docket docketFactory() {
        return new Docket(DocumentationType.SWAGGER_2).apiInfo(
                new ApiInfo("采用redis生成自增长ID值", "采用redis生成自增长id值API接口列表", "1.0", "", "", "", "")).enable(environmentSpecificBooleanFlag);
    }
}