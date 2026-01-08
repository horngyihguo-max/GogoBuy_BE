package com.example.demo.config;

import static org.springframework.security.config.Customizer.withDefaults;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

import com.example.demo.service.GoogelOAuth2Service;

// @Configuration 告訴 Spring 這是一個配置類別，程式啟動時要先讀取這裡的設定
@Configuration
@EnableWebSecurity
// 定義誰可以進入你的網站以及如何驗證身分。
public class SecurityConfig {

	@Autowired
	private GoogelOAuth2Service googelOAuth2;

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
				/*
				 * 跨來源資源共享 是一種瀏覽器機制，透過特定的 HTTP 標頭， 允許一個網域的網頁程式（如 JavaScript）安全地向不同網域的伺服器請求資源。
				 */
				.csrf(csrf -> csrf.disable()) // 關閉 CSRF，讓前端可以用 POST 傳資料
				.cors(withDefaults()) // 開啟 CORS，允許 Angular 跨網域連線

				// 1. 授權區塊
				.authorizeHttpRequests(auth -> {
					auth.anyRequest().permitAll(); // 告訴Spring所有網址都不用檢查身分
				})

				// 2. OAuth2 登入配置區塊
				.oauth2Login(oauth2 -> {
					// 自定義「發起登入」的網址
					// 設定後，前端的登入連結會變成：http://localhost:8080/login-google
					oauth2.authorizationEndpoint(authorization -> authorization.baseUri("/login/google"));

					// 交給 googelOAuth2 處理
					oauth2.userInfoEndpoint(userInfo -> userInfo.userService(googelOAuth2));

					// 登入成功後的跳轉路徑
					oauth2.defaultSuccessUrl("http://localhost:4200", true);
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