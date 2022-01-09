package pl.saqie.producttracker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableAsync(proxyTargetClass = true)
public class ProductTrackerApplication {
    public static void main(String[] args) {
        SpringApplication.run(ProductTrackerApplication.class, args);
    }
}

//TODO: 4.01 -> Lista produktów, Lista cen produktów, Odświeżanie ceny, Edycja produktu.