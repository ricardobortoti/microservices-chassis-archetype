#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.${project-name-lowercase}.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "${project-name-uppercase} not found")
public class ${project-name-uppercase}NotFoundException extends RuntimeException {

	private static final long serialVersionUID = 7825066852624562956L;
	
}