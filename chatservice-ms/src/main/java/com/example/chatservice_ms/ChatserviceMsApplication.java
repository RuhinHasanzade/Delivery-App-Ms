package com.example.chatservice_ms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class ChatserviceMsApplication {

	public static void main(String[] args) {
		SpringApplication.run(ChatserviceMsApplication.class, args);
	}

}
