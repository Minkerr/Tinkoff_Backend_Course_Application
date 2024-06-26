package edu.java.bot;

import edu.java.bot.configuration.ApplicationConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(ApplicationConfig.class)
public class BotApplication {
    private static Bot bot;

    @Autowired
    public BotApplication(Bot bot) {
        BotApplication.bot = bot;
    }

    public static void main(String[] args) {
        SpringApplication.run(BotApplication.class, args);
        bot.start();
    }
}
