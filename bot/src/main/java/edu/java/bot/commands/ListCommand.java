package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.client.ScrapperClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ListCommand implements Command {
    private ScrapperClient scrapperClient;

    @Autowired
    public ListCommand(ScrapperClient scrapperClient) {
        this.scrapperClient = scrapperClient;
    }

    @Override
    public String command() {
        return "/list";
    }

    @Override
    public String description() {
        return "show a list of tracked links";
    }

    @Override
    public SendMessage handle(Update update) {
        var links = scrapperClient.getLinks(update.message().chat().id()).links();
        StringBuilder linksStringed = new StringBuilder();
        for (var link : links) {
            linksStringed.append(link.url()).append("\n");
        }
        return new SendMessage(update.message().chat().id(), "List of tracked links:\n"
            + linksStringed);
    }
}
