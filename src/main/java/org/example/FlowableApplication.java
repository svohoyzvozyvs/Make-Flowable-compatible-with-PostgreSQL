package org.example;

import liquibase.database.DatabaseFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class FlowableApplication {

    public static void main(String[] args) {
//        DatabaseFactory.getInstance().register(new KingbaseDatabase());
        SpringApplication.run(FlowableApplication.class, args);
    }
}