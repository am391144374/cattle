package com.ktr.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
public class KtrWebApplication {

	public static void main(String[] args) {
		SpringApplication.run(KtrWebApplication.class, args);
		log.info("ktr启动完成！");
	}

}
