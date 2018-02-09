package org.finlywork.filehandle;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
@MapperScan("org.finlywork.filehandle.mapper")
public class FileHandleApplication {

	public static void main(String[] args) {
		SpringApplication.run(FileHandleApplication.class, args);
	}
}
