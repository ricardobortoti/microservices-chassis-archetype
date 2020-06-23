#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.${project-name-lowercase};

import org.junit.jupiter.api.Assertions;
import org.junit.Test;

public class ${project-name-uppercase}ApplicationTest {
	@Test
	public void contextInitialLoads() {
		Assertions.assertEquals(true,true);
	}
}