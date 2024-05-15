package vtiger.middleware.api;

import vtiger.middleware.api.config.VtigerProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({VtigerProperties.class})
public class VtigerMiddlewareApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(VtigerMiddlewareApiApplication.class, args);
	}

}
