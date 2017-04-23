package twino.domain.model.blacklist;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class BlacklistServiceImpl implements BlacklistService {

    private BlacklistRepository blacklistRepository;

    public BlacklistServiceImpl(BlacklistRepository blacklistRepository) {
        this.blacklistRepository = blacklistRepository;
    }

    @Override
    public boolean isBlacklisted(String personalId) {
        return blacklistRepository.findByPersonalId(personalId) != null;
    }

    @Override
    public BlacklistItem save(String personalId) {
        return blacklistRepository.save(new BlacklistItem(personalId));
    }

    @Override
    public Page<BlacklistItem> getAll(int page, int size) {
        return blacklistRepository.findAll(new PageRequest(page, size));
    }
}
