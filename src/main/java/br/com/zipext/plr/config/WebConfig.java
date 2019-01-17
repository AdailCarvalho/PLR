package br.com.zipext.plr.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
	
	@Value("${app.frontend.ip}")
	private String frontEndAddress;
	
	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/colaboradores").allowedOrigins(this.frontEndAddress);
		registry.addMapping("/metaEspecifica").allowedOrigins(this.frontEndAddress);
	}
}
