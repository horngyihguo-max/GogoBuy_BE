package com.example.demo.config;

import static org.springframework.security.config.Customizer.withDefaults;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

import com.example.demo.service.GoogleOAuth2;

//@Configuration 告訴 Spring 這是一個配置類別，程式啟動時要先讀取這裡的設定
//@Configuration
//@EnableWebSecurity 開啟 Spring Security 的 Web 安全支援，沒有這行，下方的設定都不會生效。
//@EnableWebSecurity
//定義誰可以進入你的網站以及如何驗證身分。
public class SecurityConfig {

	@Autowired
	private GoogleOAuth2 googleOAuth2;

	// @BEAN 這是最重要的「過濾鏈」。所有進出網站的 HTTP 請求都要經過這道門。
//	@Bean
//	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//		http
//				/*
//				 * 跨來源資源共享 是一種瀏覽器機制，透過特定的 HTTP 標頭， 允許一個網域的網頁程式（如 JavaScript）安全地向不同網域的伺服器請求資源，
//				 */
//				.cors(withDefaults()).csrf(csrf -> csrf.disable())
//				// 1. 授權區塊
//				.authorizeHttpRequests(auth -> {
//					auth.requestMatchers("/").permitAll();
//					auth.anyRequest().authenticated();
//				}).oauth2Login(oauth2 -> {
//					// 2. 告訴 Spring 拿到用戶資料後，交給 googelOAuth2 處理
//					oauth2.userInfoEndpoint(userInfo -> userInfo.userService(googleOAuth2));
//					// 登入成功後的跳轉路徑
//					oauth2.defaultSuccessUrl("/", true);
//				})
//				// 3. 登出區塊
//				.logout(logout -> {
//					logout.logoutSuccessUrl("/");
//					// 順便清除 Session 和 Cookie，確保登出乾淨
//					logout.invalidateHttpSession(true);
//					logout.deleteCookies("JSESSIONID");
//				});
//		// 最後把這些規則封裝起來交給 Spring
//		return http.build();
//	}
}
