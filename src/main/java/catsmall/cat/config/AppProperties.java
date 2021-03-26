package catsmall.cat.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Data
@ConfigurationProperties("app")
public class AppProperties {
    private String host;
    private String resourceRoot;
    private String imgPrefix;
    private String nowProfile;
}
