package cn.yuyunsoft.redis.main;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ImportResource;


/**
 *
 * @功能: Id自增长SpringBoot启动类
 * @作者: 黄小云
 * @版本: v1.0.0
 * @时间: 2016年12月10日
 * @描述: 提供id自增长服务的启动入口
 *
 */
@SpringBootApplication(exclude={DataSourceAutoConfiguration.class,HibernateJpaAutoConfiguration.class})
@ServletComponentScan
@ComponentScan(value={"cn.yuyunsoft.redis"})
@ImportResource("classpath:spring/spring-context.xml")
public class IdGeneratorMain {

    public static void main(String[] args) {
        new SpringApplicationBuilder(cn.yuyunsoft.redis.main.IdGeneratorMain.class).web(true).run(args);
    }

}