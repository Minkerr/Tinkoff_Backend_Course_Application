package edu.java.scrapper.service;

import edu.java.scrapper.dto.LinkResponse;
import edu.java.scrapper.dto.ListLinksResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ScrapperService {
    private final ChatService chatService;
    private final LinkService linkService;


    @Autowired
    public ScrapperService(ChatService chatService, LinkService linkService) {
        this.chatService = chatService;
        this.linkService = linkService;
    }

    public void registerChat(long chatId) {
        chatService.add(chatId);
    }

    public void deleteChat(Long chatId) {
        chatService.remove(chatId);
    }

    public ListLinksResponse getLinks(Long chatId) {
        var links = linkService.findAllLinks(chatId);
        return new ListLinksResponse( links.size(),
            links.stream()
                .map(elem -> new LinkResponse(elem.getId(), elem.getUrl()))
                .toList()
        );
    }

    public LinkResponse addLink(Long chatId, LinkResponse link) {
        linkService.add(chatId, link.url());
        return link;
    }

    public LinkResponse deleteLink(Long chatId, LinkResponse link) {
        linkService.remove(chatId, link.url());
        return link;
    }

}
