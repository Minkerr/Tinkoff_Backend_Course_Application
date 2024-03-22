package edu.java.bot.dto;

import java.util.List;

public record LinkUpdateResponse(
    long id,
    String url,
    List<Long> tgChatIds

) {
}
