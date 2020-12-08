#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.${project-name-lowercase}.controller;

import ${package}.${project-name-lowercase}.domain.Customer;
import ${package}.${project-name-lowercase}.service.CustomerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import javax.annotation.security.RolesAllowed;

@RestController
public class ${project-name-uppercase}Controller {
	
    private static final Logger logger = LoggerFactory.getLogger(${project-name-uppercase}Controller.class);
    @Autowired
    CustomerService customerService;

    @GetMapping(value = "/")
    public ResponseEntity<String> getExemplo() {
        logger.warn("getExample method");
        return ResponseEntity.ok("Hello World!");
    }

    @RolesAllowed("admin")
    @GetMapping("/goodbye")
    public String getGoodbye() {
        return "goodbye";
    }

    @PostAuthorize("(returnObject.name == authentication.principal.claims.get('cognito:username')) or hasRole('admin')")
    @GetMapping("/customers/{username}")
    public Customer getCustomer(@PathVariable final String username) {
        return customerService.findByUsername(username);
    }
}