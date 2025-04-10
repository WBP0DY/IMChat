package com.wbp.mallchat.common;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

/**
 * @author zhongzb
 * @date 2021/05/27
 */
@SpringBootApplication(scanBasePackages = {"com.wbp.mallchat"})
@ServletComponentScan
@MapperScan({"com.wbp.mallchat.common.**.mapper"})
public class MallchatCustomApplication {
    public static void main(String[] args) {
        SpringApplication.run(MallchatCustomApplication.class,args);
    }

}