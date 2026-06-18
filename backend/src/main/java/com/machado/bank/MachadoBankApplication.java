package com.machado.bank;

import com.machado.bank.service.IClientService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;



@SpringBootApplication
public class MachadoBankApplication implements CommandLineRunner {

	@Autowired
	private IClientService clientService;

	private static final Logger logger =
			LoggerFactory.getLogger(MachadoBankApplication.class);


	public static void main(String[] args) {
		logger.info("Iniciando la App");
		//Levantar la fabrica de spring
		SpringApplication.run(MachadoBankApplication.class, args);
		logger.info("App Rodando \uD83D\uDE80");
	}

	@Override
	public void run(String... args) throws Exception {
		logger.info("*** \uD83D\uDE80 MachadoBank App iniciada correctamente ***");
	}


}
