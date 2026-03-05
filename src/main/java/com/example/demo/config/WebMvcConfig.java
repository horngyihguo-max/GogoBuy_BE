package com.example.demo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
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
        registry.addMapping("/**")
                .allowedOriginPatterns(
                    "http://localhost:4200", 
                    "https://gogo-buy-fe.vercel.app",
                    "https://gogo-buy-*.vercel.app"
                )
                .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true)
                .maxAge(3600); // 讓瀏覽器快取 CORS 結果，減少 OPTIONS 請求次數
    }

}
