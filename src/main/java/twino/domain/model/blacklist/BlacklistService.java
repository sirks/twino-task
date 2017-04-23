package twino.domain.model.blacklist;

import org.springframework.data.domain.Page;

public interface BlacklistService {

    boolean isBlacklisted(String personalId);

    BlacklistItem save(String personalId);

    Page<BlacklistItem> getAll(int page, int size);
}
