package com.ktr.web;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

@Slf4j
@MapperScan(basePackages = "com.ktr.mapper")
@SpringBootApplication(scanBasePackages = "com.ktr.*",exclude = { DataSourceAutoConfiguration.class })
public class KtrWebApplication {

	public static void main(String[] args) {
		SpringApplication.run(KtrWebApplication.class, args);
		log.info("ktr启动完成！");
	}

}
