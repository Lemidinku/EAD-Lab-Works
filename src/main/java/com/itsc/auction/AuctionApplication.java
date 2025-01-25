package com.itsc.auction;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(exclude = {org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class})
public class AuctionApplication {

	public static void main(String[] args) {
		SpringApplication.run(AuctionApplication.class, args);
	}


	/* 
	@Bean
    public CommandLineRunner testDatabaseConnection(UserRepository repository) {
        return args -> {
            User entity = new User();
			entity.setUsername("test");
			entity.setEmail("lemidinq@gmail.com");
			entity.setPassword("password");

            repository.save(entity);

            System.out.println("Entity saved successfully: " + entity);
            System.out.println("All entities: " + repository.findAll());
        };
    }
	*/
}

