package edu.java.scrapper.service;

import edu.java.scrapper.dto.LinkResponse;
import edu.java.scrapper.dto.ListLinksResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ScrapperService {
    private final ChatService chatService;

    @Autowired
    public ScrapperService(ChatService chatService) {
        this.chatService = chatService;
    }

    public void registerChat(long chatId) {

    }

    public void deleteChat(Long chatId) {

    }

    public ListLinksResponse getLinks(Long chatId) {
        return null;
    }

    public LinkResponse addLink(Long chatId, LinkResponse link) {

        return link;
    }

    public LinkResponse deleteLink(Long chatId, LinkResponse link) {

        return link;
    }

}
