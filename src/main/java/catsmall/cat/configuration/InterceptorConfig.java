package catsmall.cat.configuration;

import catsmall.cat.interceptor.TopnavInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class InterceptorConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        InterceptorRegistration topNav = registry.addInterceptor(topnavInterceptor());
        topNav.addPathPatterns("/**");
    }
    @Bean
    public TopnavInterceptor topnavInterceptor(){
        return new TopnavInterceptor();
    }
}
