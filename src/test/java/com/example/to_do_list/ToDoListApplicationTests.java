package com.example.to_do_list;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@SpringBootTest(properties = "classpath:application-test.yml")
@ExtendWith(SpringExtension.class)
//@ActiveProfiles("prod1")
class ToDoListApplicationTests {

	@Test
	void contextLoads() {
	}

}
