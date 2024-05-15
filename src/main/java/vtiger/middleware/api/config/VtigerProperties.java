package vtiger.middleware.api.config;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Data
@Validated
@ConfigurationProperties(prefix = "teravin.vtiger")
public class VtigerProperties {

    private String url;
    private String username;
    private String adminAccessKey;

}

