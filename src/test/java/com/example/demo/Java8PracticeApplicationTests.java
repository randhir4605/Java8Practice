package com.example.demo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class Java8PracticeApplicationTests {

	@Autowired
	Practice practice;

	@Test
	void contextLoads() {
		practice.java8practice();
	}


}
