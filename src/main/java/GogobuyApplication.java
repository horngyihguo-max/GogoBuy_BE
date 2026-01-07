

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages = {"tta.project", "controller", "service", "confing", "dao", "entity"}) 
@EnableJpaRepositories(basePackages = "dao") 
@EntityScan(basePackages = "entity")  
public class GogobuyApplication {

	public static void main(String[] args) {
		SpringApplication.run(GogobuyApplication.class, args);
	}

}
