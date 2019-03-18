package com.javastream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.telegram.telegrambots.ApiContextInitializer;

import javax.annotation.PostConstruct;

@SpringBootApplication
public class BotSpringTelegramApplication {


	public static void main(String[] args) {
		ApiContextInitializer.init();
		SpringApplication.run(BotSpringTelegramApplication.class, args);
	}


}
