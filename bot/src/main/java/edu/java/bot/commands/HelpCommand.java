package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class HelpCommand implements Command {
    private final List<Command> commands;

    public HelpCommand(List<Command> commands) {
        this.commands = commands.stream().filter(command -> !command.command().equals("/unknown")).toList();
    }

    @Override
    public String command() {
        return "/help";
    }

    @Override
    public String description() {
        return "shows available commands";
    }

    @Override
    public SendMessage handle(Update update) {
        return new SendMessage(update.message().chat().id(), getCommandList(commands));
    }

    private String getCommandList(List<Command> commands) {
        StringBuilder result = new StringBuilder();
        for (var command : commands) {
            result.append(command.command());
            result.append(" — ");
            result.append(command.description());
            result.append("\n");
        }
        return result.toString();
    }
}
