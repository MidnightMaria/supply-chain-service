package com.agnesmaria.supplychain;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;

@SpringBootApplication
public class SupplyChainServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(SupplyChainServiceApplication.class, args);
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Supply Chain Service API")
                        .version("1.0.0")
                        .description("Microservice for managing suppliers and purchase orders in supply chain system")
                        .contact(new Contact()
                                .name("Agnes Maria")
                                .email("agnesmaria@example.com")));
    }
}