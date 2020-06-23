#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.${project-name-lowercase};

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ${project-name-uppercase}Application {
	
	public static void main(String[] args) {
		SpringApplication.run(${project-name-uppercase}Application.class, args);
	}
	
}