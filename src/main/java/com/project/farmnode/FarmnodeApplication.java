package com.project.farmnode;

import com.project.farmnode.model.Role;
import com.project.farmnode.model.User;
import com.project.farmnode.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;

@SpringBootApplication
public class FarmnodeApplication {

	public static void main(String[] args) {
		SpringApplication.run(FarmnodeApplication.class, args);
	}

	@Bean
	PasswordEncoder passwordEncoder(){
		return new BCryptPasswordEncoder();
	}

	@Bean
	CommandLineRunner run(UserService userService){
		return args -> {
			userService.saveRole(new Role(null,"ROLE_USER"));
			userService.saveRole(new Role(null,"ROLE_ADMIN"));

			userService.saveUser(new User(null,"Abshalom Judah","judah","1234",new ArrayList<>()));
			userService.saveUser(new User(null,"Ashni Juliana","julie","1234",new ArrayList<>()));

			userService.addRoleToUser("judah","ROLE_USER");
			userService.addRoleToUser("judah","ROLE_ADMIN");
			userService.addRoleToUser("julie","ROLE_USER");
		};
	}

}
