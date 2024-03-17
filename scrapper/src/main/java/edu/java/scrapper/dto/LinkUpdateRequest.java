package edu.java.scrapper.dto;

import java.util.List;

public record LinkUpdateRequest(
    long id,
    String url,
    List<Long> tgChatIds

) {

}
