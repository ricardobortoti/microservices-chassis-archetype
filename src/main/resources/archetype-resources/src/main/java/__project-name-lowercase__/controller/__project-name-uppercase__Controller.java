#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.${project-name-lowercase}.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ${project-name-uppercase}Controller {
	
    private static final Logger logger = LoggerFactory.getLogger(${project-name-uppercase}Controller.class);

    @GetMapping(value = "/")
    public ResponseEntity<String> getExemplo() {
        logger.warn("getExemple method");
        return ResponseEntity.ok("Hello World!");
    }
    
}