package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestConfig {
	
	@Bean // 啟動時，Spring 會執行這個方法，並把回傳的物件存起來
    public RestTemplate restTemplate() {
        return new RestTemplate(); 
    }

}
