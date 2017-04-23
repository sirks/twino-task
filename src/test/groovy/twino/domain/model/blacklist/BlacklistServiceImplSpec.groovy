package twino.domain.model.blacklist

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import spock.lang.Specification

class BlacklistServiceImplSpec extends Specification {

    def blacklistRepository = Mock BlacklistRepository

    def service = new BlacklistServiceImpl(blacklistRepository)

    def personalId = "persid"
    def page = 11
    def size = 22

    def "returns the true or false if personal id is blacklisted"() {
        when:
        def result = service.isBlacklisted(personalId)

        then:
        1 * blacklistRepository.findByPersonalId(personalId) >> new BlacklistItem()
        result
    }

    def "saves personal id to blacklist"() {
        given:
        def savedBlacklistItem = new BlacklistItem()

        when:
        def result = service.save(personalId)

        then:
        1 * blacklistRepository.save({ it.personalId == personalId } as BlacklistItem) >> savedBlacklistItem
        result.is(savedBlacklistItem)
    }

    def "returns the page with items from blacklist"() {
        given:
        def pageResult = Mock Page

        when:
        def result = service.getAll(page, size)

        then:
        1 * blacklistRepository.findAll({ it.pageNumber == page && it.pageSize == size } as Pageable) >> pageResult
        result == pageResult
    }
}
