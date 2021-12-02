package com.bolzano.Task03;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * The main java class for running the REST Application
 * Used to enable auto-configuration, component scan and be able to define extra configuration
 * @author Tharindu Madhusankha - 3198602
 */

@SpringBootApplication
public class ProductrestApplication {
	public static void RestApiMain() {
		SpringApplication.run(ProductrestApplication.class);
	}

}
