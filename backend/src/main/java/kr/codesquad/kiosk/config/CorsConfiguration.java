package kr.codesquad.kiosk.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration(proxyBeanMethods = false)
public class CorsConfiguration implements WebMvcConfigurer {

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**")
				.allowedOrigins("http://13.124.226.31:80",
						"http://43.201.168.11:8080",
						"http://localhost:3000",
						"http://localhost:8080",
						"http://127.0.0.1:3000")
				.allowedMethods("GET", "POST", "OPTION", "PUT");
	}
}
