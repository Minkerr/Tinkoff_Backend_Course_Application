package edu.java.bot;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import java.util.List;
import java.util.stream.Stream;
import edu.java.bot.client.ScrapperClient;
import edu.java.bot.commands.Command;
import edu.java.bot.commands.HelpCommand;
import edu.java.bot.commands.ListCommand;
import edu.java.bot.commands.StartCommand;
import edu.java.bot.commands.TrackCommand;
import edu.java.bot.commands.UnknownCommand;
import edu.java.bot.commands.UntrackCommand;
import edu.java.bot.configuration.ApplicationConfig;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
public class CommandTest {
    private static WireMockServer wireMockServer;
    private static ScrapperClient scrapperClient;
    private static final String baseUrl = "http://localhost:8080";
    @BeforeAll
    public static void setUp() {
        scrapperClient = new ScrapperClient(baseUrl);
        wireMockServer = new WireMockServer(8080);
        wireMockServer.start();
    }

    @AfterAll
    public static void tearDown() {
        wireMockServer.stop();
    }


    private List<Command> commandsWithoutHelp = List.of(
        new StartCommand(scrapperClient),
        new TrackCommand(),
        new ListCommand(scrapperClient),
        new UntrackCommand(scrapperClient),
        new UnknownCommand()
    );

    private List<Command> commands = Stream.concat(
            commandsWithoutHelp.stream(),
            Stream.of(new HelpCommand(commandsWithoutHelp))
        )
        .toList();

    private final LinkValidator validator = new LinkValidator();
    private final CommandRecognizer recognizer = new CommandRecognizer(commands, validator, scrapperClient);
    private final UserMessageListener listener = new UserMessageListener(recognizer);
    private final Bot bot = new Bot(Mockito.mock(ApplicationConfig.class), listener, commands);

    private Update getMockUpdate(String text) {
        var mock = Mockito.mock(Update.class);
        Mockito.when(mock.message()).thenReturn(Mockito.mock(Message.class));
        Mockito.when(mock.message().chat()).thenReturn(Mockito.mock(Chat.class));
        Mockito.when(mock.message().chat().id()).thenReturn(1L);
        Mockito.when(mock.message().text()).thenReturn(text);
        return mock;
    }

    @Test
    @Disabled
    void recognize_shouldRecognizeCommand() {
        //arrange
        Update mock = getMockUpdate("/start");
        //act
        var act = recognizer.recognize(mock);
        //assert
        assertThat(act).isInstanceOf(StartCommand.class);
    }

    @Test
    @Disabled
    void process_shouldReturnConfirmedUpdatesAll() {
        //arrange
        Update mock = getMockUpdate("/help");
        //act
        var act = bot.process(List.of(mock));
        //assert
        assertThat(act).isEqualTo(UpdatesListener.CONFIRMED_UPDATES_ALL);
    }

    @Test
    @Disabled
    void recognizeCommand_shouldRecognizeStartCommand() {
        //arrange
        Update mock = getMockUpdate("/start");
        String exp = "You are registered for resource tracking";
        //act
        var act = recognizer.recognizeCommand(mock).getParameters().get("text");
        //assert
        assertThat(act).isEqualTo(exp);
    }

    @Test
    @Disabled
    void recognizeCommand_shouldRecognizeTrackCommand() {
        //arrange
        Update mock = getMockUpdate("/track");
        String exp = "Input link for tracking:";
        //act
        var act = recognizer.recognizeCommand(mock).getParameters().get("text");
        //assert
        assertThat(act).isEqualTo(exp);
    }

    @Test
    @Disabled
    void recognizeCommand_shouldRecognizeUnknownCommand() {
        //arrange
        Update mock = getMockUpdate("/hello");
        String exp = "Unknown command. Use /help to see the available commands";
        //act
        var act = recognizer.recognizeCommand(mock).getParameters().get("text");
        //assert
        assertThat(act).isEqualTo(exp);
    }

    @Test
    @Disabled
    void linkValidationInDialog_shouldMatchCorrectLinks() {
        //arrange
        Update mock = getMockUpdate("https://stackoverflow.com/questions/1");
        String exp = "Link successfully added for tracking!";
        //act
        var act = recognizer.linkValidationInDialog(mock).getParameters().get("text");
        //assert
        assertThat(act).isEqualTo(exp);
    }

    @Test
    @Disabled
    void linkValidationInDialog_shouldMatchIncorrectLinks() {
        //arrange
        Update mock = getMockUpdate("https://stackoverflow.com/questions/");
        String exp = "Incorrect input";
        //act
        var act = recognizer.linkValidationInDialog(mock).getParameters().get("text");
        //assert
        assertThat(act).isEqualTo(exp);
    }
}
