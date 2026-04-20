package com.marmitaria;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "Marmitaria Leozitos API", version = "1.0", description = "API para gerenciamento de marmitas e pedidos"))
public class Main {
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }
}