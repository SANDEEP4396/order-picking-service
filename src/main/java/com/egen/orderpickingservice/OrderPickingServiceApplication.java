package com.egen.orderpickingservice;

import com.egen.orderpickingservice.configuration.SwaggerConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
@Import(SwaggerConfiguration.class)
public class OrderPickingServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(OrderPickingServiceApplication.class, args);
	}

}
