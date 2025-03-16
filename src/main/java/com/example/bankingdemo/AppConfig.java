package com.example.bankingdemo;


import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class AppConfig {

    @Bean
    public OpenAPI defineOpenApi() {
        Server server = new Server();
//        server.setUrl("http://localhost:8080");
        server.setUrl("/");
        server.setDescription("Development of basic banking  application");

        Contact myContact = new Contact();
        myContact.setName("Bola Ajagz");
        myContact.setEmail("bolaajagunna@gmail.com");

        Info information = new Info()
                .title("Banking Application")
                .version("1.0")
                .description("This API exposes endpoints to to do level 1 banking process.")
                .contact(myContact);
        return new OpenAPI().info(information).servers(List.of(server));
    }
}
