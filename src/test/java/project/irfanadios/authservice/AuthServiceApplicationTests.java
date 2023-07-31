package project.irfanadios.authservice;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class AuthServiceApplicationTests {

	@Test
	void contextLoads() {
		var a = true;
		var b = true;
		assertEquals(a, b);
	}

}
