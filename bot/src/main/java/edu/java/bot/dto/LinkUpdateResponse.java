package edu.java.bot.dto;

import java.util.List;

public record LinkUpdateResponse(
    long id,
    String url,
    String description,
    List<Long> telegramChatIds

) {
}
