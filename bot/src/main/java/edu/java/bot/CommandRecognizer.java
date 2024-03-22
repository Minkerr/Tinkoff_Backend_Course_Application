package edu.java.bot;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.client.ScrapperClient;
import edu.java.bot.commands.Command;
import edu.java.bot.commands.TrackCommand;
import edu.java.bot.dto.AddLinkRequest;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CommandRecognizer {
    private final Map<String, Command> commands;
    private final LinkValidator validator;
    private final ScrapperClient scrapperClient;
    private boolean dialogState = false;

    @Autowired
    public CommandRecognizer(List<Command> commands, LinkValidator validator, ScrapperClient client) {
        this.scrapperClient = client;
        this.validator = validator;
        this.commands = commands
            .stream()
            .collect(Collectors.toMap(Command::command, Function.identity()));
    }

    public Command recognize(Update update) {
        String messageText = update.message().text();
        Command recognizedCommand = commands.get(messageText);
        if (recognizedCommand == null) {
            recognizedCommand = commands.get("/unknown");
        }
        return recognizedCommand;
    }

    public SendMessage recognizeCommand(Update update) {
        Command command = recognize(update);
        if (command.getClass().equals(TrackCommand.class)) {
            dialogState = true;
        }
        return command.handle(update);
    }

    public SendMessage linkValidationInDialog(Update update) {
        String link = update.message().text();
        String messageText = validator.validate(link);
        if (validator.isLinkCorrect(link)) {
            scrapperClient.addLink(update.message().chat().id(), new AddLinkRequest(link)); // ADD LINK TO DB
            dialogState = false;
        }
        return new SendMessage(update.message().chat().id(), messageText);
    }

    public boolean getDialogState() {
        return dialogState;
    }
}
