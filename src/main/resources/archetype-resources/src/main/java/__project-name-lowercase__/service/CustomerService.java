#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.${project-name-lowercase}.service;

import ${package}.${project-name-lowercase}.domain.Customer;
import ${package}.${project-name-lowercase}.exception.CustomerNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {

    public Customer findByUsername(String username) {

        switch (username) {
            case "jane-doe":
                return new Customer("jane-doe", "jane-doe@mailserver.com", "867-5309", "9d7db95a-5793-4963-a40b-b6cd65a26781");
            case "john-doe":
                return new Customer("john-doe", "john-doe@mailserver.com", "735-5507", "45a0c337-6d1d-44d4-8368-40a3fb17c072");
            case "chuck-norris":
                return new Customer("chuck-norris", "chuck-norris@mailserver.com", "911", "c1d6b919-8159-48fc-9a86-05f02836d9b7");
            default:
                throw new CustomerNotFoundException();
        }
    }

}