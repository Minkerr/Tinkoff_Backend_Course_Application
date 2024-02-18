package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.Command;

public class UntrackCommand implements Command {
    @Override
    public String command() {
        //remove link from db
        return "untrack";
    }

    @Override
    public String description() {
        return "/untrack — stop tracking the link";
    }

    @Override
    public SendMessage handle(Update update) {
        return new SendMessage(update.message().chat().id(), command());
    }
}

