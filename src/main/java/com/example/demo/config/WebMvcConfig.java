package com.example.demo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.example.demo.Interceptor.UserStatusInterceptor;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

	@Autowired
	private UserStatusInterceptor userStatusInterceptor;

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(userStatusInterceptor).addPathPatterns("/gogobuy/**") // 攔截所有 API
				.excludePathPatterns(
						"/gogobuy/user/login",
						"/gogobuy/user/registration",
						"/gogobuy/user/active-account", // 驗證信連結
						"/gogobuy/user/oauth", // Google OAuth
						"/gogobuy/user/send-otp", // 發送 OTP
						"/gogobuy/user/send-otp-email", // 忘記密碼發送 OTP
						"/gogobuy/user/reset-password", // 重設密碼
						"/gogobuy/user/email-verify" // 驗證並更新 Email
				);
	}

	@Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // 所有的 API 路徑
                .allowedOrigins(
                    "http://localhost:4200", 
                    "https://gogo-buy-8aa73bf91-horngyihguo-3517s-projects.vercel.app" // 你的 Vercel 網址
                )
                .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true); // 因為你在前端有寫 withCredentials: true
    }
	
	@Bean
	public CorsFilter corsFilter() {
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", corsConfig());
		return new CorsFilter(source);
	}

	private CorsConfiguration corsConfig() {
		CorsConfiguration corsConfiguration = new CorsConfiguration();
		corsConfiguration.addAllowedOriginPattern("*");
		corsConfiguration.addAllowedHeader("*");
		corsConfiguration.addAllowedMethod("*");
		corsConfiguration.setAllowCredentials(true);
		return corsConfiguration;
	}

}
