package edu.java.bot.dto;

import jakarta.validation.constraints.NotNull;

public record LinkResponse(
    long id,
    @NotNull
    String url
) {
}
