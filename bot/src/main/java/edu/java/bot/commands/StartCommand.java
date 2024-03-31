package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.client.ScrapperClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class StartCommand implements Command {
    private ScrapperClient scrapperClient;

    @Autowired
    public StartCommand(ScrapperClient scrapperClient) {
        this.scrapperClient = scrapperClient;
    }

    @Override
    public String command() {
        return "/start";
    }

    @Override
    public String description() {
        return "register new user for tracking";
    }

    @Override
    public SendMessage handle(Update update) {
        scrapperClient.registerChat(update.message().chat().id());
        return new SendMessage(update.message().chat().id(), "You are registered for resource tracking");
    }
}
