package edu.java.bot.controller;

import edu.java.bot.dto.LinkUpdateResponse;
import edu.java.bot.service.BotUpdateService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BotController {
    private BotUpdateService botUpdateService;
    private static final Logger LOGGER = LogManager.getLogger();

    @PostMapping("/updates")
    public void processUpdate(@RequestBody LinkUpdateResponse linkUpdateRequest) {
        LOGGER.info("updates are added");

    }
}
