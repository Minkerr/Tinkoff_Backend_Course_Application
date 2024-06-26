package edu.java.bot;

import java.util.regex.Pattern;
import org.springframework.stereotype.Component;

@Component
public class LinkValidator {
    public String validate(String link) {
        if (isLinkCorrect(link)) {
            return "Link successfully added for tracking!";
        } else {
            return "Incorrect input (type cancel to cancel the link entry)";
        }
    }

    public boolean isLinkCorrect(String link) {
        return Pattern.matches("^https:\\/\\/stackoverflow\\.com\\/questions\\/\\d+\\/?", link)
            || Pattern.matches("https:\\/\\/github\\.com\\/[(a-zA-Z0-9_]+\\/[(a-zA-Z0-9_-]+\\/?", link);
    }
}
