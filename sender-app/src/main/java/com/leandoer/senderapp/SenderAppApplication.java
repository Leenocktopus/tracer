package com.leandoer.senderapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;


@ComponentScan({"com.leandoer"})
@SpringBootApplication
public class SenderAppApplication {


	public static void main(String[] args) {
		SpringApplication.run(SenderAppApplication.class, args);
	}

}
