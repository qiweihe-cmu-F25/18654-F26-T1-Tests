package userprofile;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
/** Entry point of the service.
 *  Spring Boot annotations:
 *  {@code @SpringBootApplication} is used to bootstrap and launch a Spring application from a Java main method, by invoking SpringApplication.run() method. Spring Boot classes are autodetected by spring framework through classpath scanning.
 *  Spring boot handles object initialization and dependency injection automatically through annotations {@code (@Controller, @Service, @Repository)} and constructor injection. This allows you to focus on writing business logic and handling requests without explicitly creating instances or managing dependencies manually.
 */
@SpringBootApplication(exclude = {
		org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class}
)
public class UserProfileServiceApp {

	public static void main(String[] args) {
		SpringApplication.run(UserProfileServiceApp.class, args);
	}

}
