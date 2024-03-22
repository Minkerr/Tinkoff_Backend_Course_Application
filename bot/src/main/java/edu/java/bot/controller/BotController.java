package edu.java.bot.controller;

import edu.java.bot.dto.LinkUpdateRequest;
import edu.java.bot.service.BotUpdateService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BotController {
    private BotUpdateService botUpdateService;
    private static final Logger LOGGER = LogManager.getLogger();

    @Autowired
    public BotController(BotUpdateService botUpdateService) {
        this.botUpdateService = botUpdateService;
    }

    @PostMapping("/updates")
    public void processUpdate(@RequestBody LinkUpdateRequest linkUpdateRequest) {
        LOGGER.info("updates are added");
        botUpdateService.processRequest(linkUpdateRequest);
    }
}
