package edu.java.bot;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserMessageListener implements UserMessageProcessor {
    private final CommandRecognizer recognizer;

    @Autowired
    public UserMessageListener(CommandRecognizer recognizer) {
        this.recognizer = recognizer;
    }

    @Override
    public SendMessage process(Update update) {
        return recognizer.getDialogState()
            ? recognizer.linkProcessingInDialog(update) : recognizer.handleCommand(update);
    }
}
