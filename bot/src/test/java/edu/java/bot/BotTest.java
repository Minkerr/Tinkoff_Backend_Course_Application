package edu.java.bot;

import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import java.util.List;
import edu.java.bot.commands.HelpCommand;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class BotTest {
    private Update getMockUpdate(String text) {
        var mock = Mockito.mock(Update.class);
        Mockito.when(mock.message()).thenReturn(Mockito.mock(Message.class));
        Mockito.when(mock.message().chat()).thenReturn(Mockito.mock(Chat.class));
        Mockito.when(mock.message().chat().id()).thenReturn(1L);
        Mockito.when(mock.message().text()).thenReturn(text);
        return mock;
    }

    @Test
    void process_shouldReturnConfirmedUpdatesAll() {
        //arrange
        Update mock = getMockUpdate("/help");
        //act
        Bot bot = new Bot("test");
        var act = bot.process(List.of(mock));
        //assert
        assertThat(act).isEqualTo(UpdatesListener.CONFIRMED_UPDATES_ALL);
    }

    @Test
    void recognizeCommand_shouldRecognizeStartCommand() {
        //arrange
        Update mock = getMockUpdate("/start");
        String exp = "You are registered for resource tracking";
        //act
        Bot bot = new Bot("test");
        var act = bot.recognizeCommand(mock);
        //assert
        assertThat(act).isEqualTo(exp);
    }

    @Test
    void recognizeCommand_shouldRecognizeTrackCommand() {
        //arrange
        Update mock = getMockUpdate("/track");
        String exp = "Input link for tracking:";
        //act
        Bot bot = new Bot("test");
        var act = bot.recognizeCommand(mock);
        //assert
        assertThat(act).isEqualTo(exp);
    }

    @Test
    void recognizeCommand_shouldRecognizeUnknownCommand() {
        //arrange
        Update mock = getMockUpdate("/hello");
        String exp = "Unknown command. Use /help to see the available commands";
        //act
        Bot bot = new Bot("test");
        var act = bot.recognizeCommand(mock);
        //assert
        assertThat(act).isEqualTo(exp);
    }

    @Test
    void recognizeCommand_shouldRecognizeHelpCommand() {
        //arrange
        Update mock = getMockUpdate("/help");
        HelpCommand com = new HelpCommand();
        String exp = com.command();
        //act
        Bot bot = new Bot("test");
        var act = bot.recognizeCommand(mock);
        //assert
        assertThat(act).isEqualTo(exp);
    }

    @Test
    void linkValidationInDialog_shouldMatchCorrectLinks() {
        //arrange
        Update mock = getMockUpdate("https://stackoverflow.com/questions/1");
        HelpCommand com = new HelpCommand();
        String exp = "Link successfully added for tracking!";
        //act
        Bot bot = new Bot("test");
        var act = bot.linkValidationInDialog(mock);
        //assert
        assertThat(act).isEqualTo(exp);
    }

    @Test
    void linkValidationInDialog_shouldMatchInorrectLinks() {
        //arrange
        Update mock = getMockUpdate("https://stackoverflow.com/questions/");
        HelpCommand com = new HelpCommand();
        String exp = "Incorrect input";
        //act
        Bot bot = new Bot("test");
        var act = bot.linkValidationInDialog(mock);
        //assert
        assertThat(act).isEqualTo(exp);
    }
}
