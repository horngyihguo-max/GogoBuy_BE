package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.scheduling.annotation.EnableScheduling;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import java.util.List;

@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
@EnableScheduling
public class GogobuyApplication {
	@Bean
	public OpenAPI customOpenAPI() {
		return new OpenAPI()
				.servers(List.of(new Server().url("https://gogobuybe-production.up.railway.app").description("生產環境"),
						new Server().url("http://localhost:8080").description("本地測試")));
	}

	public static void main(String[] args) {
		SpringApplication.run(GogobuyApplication.class, args);
	}

}
