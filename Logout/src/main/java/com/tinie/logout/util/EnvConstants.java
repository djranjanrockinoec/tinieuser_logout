package com.tinie.logout.util;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("app.constants")
@Data
public class EnvConstants {
    private String tokenSecret;
}
