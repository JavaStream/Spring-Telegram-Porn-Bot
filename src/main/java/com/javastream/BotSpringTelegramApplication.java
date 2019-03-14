package com.javastream;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.telegram.telegrambots.ApiContextInitializer;

@SpringBootApplication
public class BotSpringTelegramApplication {

	public static void main(String[] args) {
		ApiContextInitializer.init();
		SpringApplication.run(BotSpringTelegramApplication.class, args);
	}
}
