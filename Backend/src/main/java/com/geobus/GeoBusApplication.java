package com.geobus;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling // necesario para la carga nocturna del GTFS
public class GeoBusApplication {

    public static void main(String[] args) {
        SpringApplication.run(GeoBusApplication.class, args);
    }
}
