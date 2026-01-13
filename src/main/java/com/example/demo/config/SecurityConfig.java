package com.example.demo.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.example.demo.service.GoogleOAuth2Service;

// @Configuration 告訴 Spring 這是一個配置類別，程式啟動時要先讀取這裡的設定
@Configuration
@EnableWebSecurity
// 定義誰可以進入你的網站以及如何驗證身分。
public class SecurityConfig {

	@Autowired
	private GoogleOAuth2Service googleOAuth2;

	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration config = new CorsConfiguration();
		config.setAllowedOrigins(List.of("http://localhost:4200")); // Angular 端口
		config.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH","OPTIONS"));
		config.setAllowedHeaders(List.of("*"));
		config.setAllowCredentials(true); // 允許攜帶 Cookie (JSESSIONID)

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", config);
		return source;
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
				/*
				 * 跨來源資源共享 是一種瀏覽器機制，透過特定的 HTTP 標頭， 允許一個網域的網頁程式（如 JavaScript）安全地向不同網域的伺服器請求資源。
				 */
				.csrf(csrf -> csrf.disable()) // 關閉 CSRF，讓前端可以用 POST 傳資料
				.cors(cors -> cors.configurationSource(corsConfigurationSource())) // 開啟 CORS，允許 Angular 跨網域連線

				// 1. 授權區塊
				.authorizeHttpRequests(auth -> auth
						// 繞過免google授權的
						.requestMatchers("/", "/**", "/oauth2/**", "/login/**", "/swagger-ui/**", "/gogobuy/user/oauth", "/oauth2/**").permitAll())
				// 2. OAuth2 登入配置區塊
				.oauth2Login(oauth2 -> {
					// 交給 googelOAuth2 處理
					oauth2.userInfoEndpoint(userInfo -> userInfo.userService(googleOAuth2));
					// 登入成功後的跳轉路徑
					oauth2.defaultSuccessUrl("http://localhost:4200/auth-callback", true);
				})

				// 3. 登出區塊
				.logout(logout -> {
					// 登出成功後同樣跳回前端首頁
					logout.logoutSuccessUrl("http://localhost:4200");
					// 順便清除 Session 和 Cookie，確保登出乾淨
					logout.invalidateHttpSession(true);
					logout.deleteCookies("JSESSIONID");
				});
		return http.build();
	}
}