package com.cattle.web;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@Slf4j
@MapperScan(basePackages = "com.cattle.mapper")
@SpringBootApplication(scanBasePackages = {"com.cattle","cn.hutool.extra.spring"},exclude = { DataSourceAutoConfiguration.class })
public class KtrWebApplication {

	public static void main(String[] args) {
		SpringApplication.run(KtrWebApplication.class, args);
		log.info("Cattle 启动完成！");
	}

}
