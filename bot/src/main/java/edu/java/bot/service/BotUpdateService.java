package edu.java.bot.service;

import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.Bot;
import edu.java.bot.dto.LinkUpdateRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BotUpdateService {
    private final Bot bot;

    @Autowired
    public BotUpdateService(Bot bot) {
        this.bot = bot;
    }

    public void processRequest(LinkUpdateRequest request) {
        String message = "There has been a change in the link: \n" + request.url();

        request.tgChatIds().forEach((telegramId) ->
            bot.execute(new SendMessage(telegramId, message)));
    }
}
