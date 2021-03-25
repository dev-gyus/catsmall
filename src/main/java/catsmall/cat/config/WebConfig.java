package catsmall.cat.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.ServletContext;

@Profile("local")
@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {
    private final AppProperties appProperties;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String resourceRoot = appProperties.getResourceRoot();
        registry.addResourceHandler("/upload/images/**")
                .addResourceLocations(resourceRoot + "image/");
        registry.addResourceHandler("/upload/thumbnail/**")
                .addResourceLocations(resourceRoot + "thumbnail/");
        registry.addResourceHandler("/upload/review/**")
                .addResourceLocations(resourceRoot + "review/");
    }


    @Bean
    public CommonsMultipartResolver multipartResolver(){
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
        multipartResolver.setDefaultEncoding("UTF-8");
        multipartResolver.setMaxUploadSizePerFile(5 * 1024 * 1024); // 5MB
        return multipartResolver;
    }
}
