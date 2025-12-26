package io.jaamong.recordMCI;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class RecordMciApplication {

	public static void main(String[] args) {
		SpringApplication.run(RecordMciApplication.class, args);
	}

}
