package edu.java.scrapper.domain.service.services;

import edu.java.scrapper.domain.dao.Link;

public interface LinkHandler {
    boolean checkLinkForUpdates(Link link);
}
