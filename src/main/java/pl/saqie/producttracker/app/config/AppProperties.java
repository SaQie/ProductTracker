package pl.saqie.producttracker.app.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "app")
@Getter
@Setter
public class AppProperties {
    private String applicationAdress;
    private long saveCooldownInMinutes;
    private long activationTokenExpiredInMinutes;
    private long refreshCooldownInMinutes;
}
