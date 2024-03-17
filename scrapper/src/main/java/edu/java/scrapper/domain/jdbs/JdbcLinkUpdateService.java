package edu.java.scrapper.domain.jdbs;

import edu.java.scrapper.domain.service.LinkUpdateService;
import org.springframework.stereotype.Service;

@Service
public class JdbcLinkUpdateService implements LinkUpdateService {
    @Override
    public int update() {
        return 0;
    }
}
