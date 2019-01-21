package br.com.zipext.plr.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {
	
	@Value("${app.frontend.ext.ip}")
	private String frontEndAddressExt;
	
	@Value("${app.frontend.int.ip}")
	private String frontEndAddressInt;
	
	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/colaboradores/**")
			.allowedOrigins(this.frontEndAddressInt, this.frontEndAddressExt)
			.allowedMethods("GET","POST","DELETE","PUT","HEAD","OPTIONS");
		registry.addMapping("/metaEspecifica/**")
			.allowedOrigins(this.frontEndAddressInt, this.frontEndAddressExt)
			.allowedMethods("GET","POST","DELETE","PUT","HEAD","OPTIONS");
	}
}
