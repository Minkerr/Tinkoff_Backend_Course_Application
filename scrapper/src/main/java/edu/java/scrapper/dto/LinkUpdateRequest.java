package edu.java.scrapper.dto;

import java.util.List;

public record LinkUpdateRequest(
    long id,
    String url,
    String description,
    List<Long> tgChatIds

) {

}
