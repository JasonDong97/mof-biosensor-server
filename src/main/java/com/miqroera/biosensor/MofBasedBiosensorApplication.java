package com.miqroera.biosensor;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.miqroera.biosensor.**.mapper")
public class MofBasedBiosensorApplication {

	public static void main(String[] args) {
		SpringApplication.run(MofBasedBiosensorApplication.class, args);
	}

}
