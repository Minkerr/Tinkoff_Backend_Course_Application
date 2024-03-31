package edu.java.bot;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.client.ScrapperClient;
import edu.java.bot.commands.Command;
import edu.java.bot.commands.TrackCommand;
import edu.java.bot.commands.UntrackCommand;
import edu.java.bot.dto.AddLinkRequest;
import edu.java.bot.dto.RemoveLinkRequest;
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
    private int dialogState = 0;
    private static final int COMMON_STATE = 0;
    private static final int TRACK_STATE = 1;
    private static final int UNTRACK_STATE = 2;

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

    public SendMessage handleCommand(Update update) {
        Command command = recognize(update);
        if (command.getClass().equals(TrackCommand.class)) {
            dialogState = TRACK_STATE;
        }
        if (command.getClass().equals(UntrackCommand.class)) {
            dialogState = UNTRACK_STATE;
        }
        return command.handle(update);
    }

    public SendMessage linkProcessingInDialog(Update update) {
        String link = update.message().text();
        if (link.equals("cancel")) {
            dialogState = COMMON_STATE;
            return new SendMessage(update.message().chat().id(), "Link entry has canceled");
        } else if (dialogState == TRACK_STATE) {
            return processTrackCommand(update);
        } else {
            return processUntrackCommand(update);
        }
    }

    private SendMessage processTrackCommand(Update update) {
        String link = update.message().text();
        String messageText = validator.validate(link);
        if (validator.isLinkCorrect(link)) {
            scrapperClient.addLink(update.message().chat().id(), new AddLinkRequest(link)); // ADD LINK TO DB
            dialogState = COMMON_STATE;
        }
        return new SendMessage(update.message().chat().id(), messageText);
    }

    private SendMessage processUntrackCommand(Update update) {
        String link = update.message().text();
        scrapperClient.removeLink(update.message().chat().id(), new RemoveLinkRequest(link)); // DELETE LINK FROM DB
        dialogState = COMMON_STATE;
        return new SendMessage(update.message().chat().id(), "Link deleted");
    }

    public boolean getDialogState() {
        return dialogState != COMMON_STATE;
    }
}
