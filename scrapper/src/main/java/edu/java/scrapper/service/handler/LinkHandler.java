package edu.java.scrapper.service.handler;

import edu.java.scrapper.domain.model.Link;

public interface LinkHandler {
    boolean checkLinkForUpdates(Link link);
}
