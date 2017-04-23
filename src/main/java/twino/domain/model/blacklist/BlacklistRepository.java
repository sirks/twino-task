package twino.domain.model.blacklist;

import org.springframework.data.repository.PagingAndSortingRepository;

public interface BlacklistRepository extends PagingAndSortingRepository<BlacklistItem, Long> {

    BlacklistItem findByPersonalId(String personalId);
}
