package com.sherpa.jodaeri.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns(
                        "http://127.0.0.1:3000",
                        "http://localhost:3000",
                        "https://jodaeri.vercel.app",
                        "https://jodaeri-bz9mktmmx-jjs-projects-d0f0b737.vercel.app/",
                        "chrome-extension://cekenpinaifhijpikjjkngfiljpkgkjd",
                        // 허용 사이트
                        "https://www.signgate.com",
                        "https://www.signkorea.com ",
                        "https://www.crosscert.com",
                        "https://www.tradesign.net",
                        "https://www.g2b.go.kr",
                        "https://www.pps.go.kr"
                )
                .allowedMethods("*")
                .allowedHeaders("*")
                .allowCredentials(true);
    }
}