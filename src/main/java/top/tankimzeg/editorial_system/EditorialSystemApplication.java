package top.tankimzeg.editorial_system;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
public class EditorialSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(EditorialSystemApplication.class, args);
	}

}
