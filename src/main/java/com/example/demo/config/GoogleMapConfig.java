package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.google.maps.GeoApiContext;

@Configuration
public class GoogleMapConfig {

	@Bean
	//工具人物件（GeoApiContext），讓它跟 Google 溝通
    public GeoApiContext geoApiContext() {
        return new GeoApiContext.Builder()
                .apiKey("AIzaSyABOHXB73JqQ17_RwtbYMyinkbT6XGQBKA") 
                .build();
    }
}
