package edu.java.scrapper.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;

public record LinkUpdateRequest(
    long id,
    String url,

    @NotEmpty
    @JsonProperty("tgChatIds")
    List<Long> tgChatIds

) {

}
