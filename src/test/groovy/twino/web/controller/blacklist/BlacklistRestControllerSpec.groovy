package twino.web.controller.blacklist

import org.springframework.data.domain.Page
import twino.domain.model.blacklist.BlacklistService
import spock.lang.Specification

class BlacklistRestControllerSpec extends Specification {

    def blacklistService = Mock BlacklistService

    def controller = new BlacklistRestController(blacklistService)

    def personalId = "persid"
    def page = 11
    def size = 22

    def "adds personal id to blacklist"() {
        when:
        controller.addToBlacklist(personalId)

        then:
        1 * blacklistService.save(personalId)
    }

    def "returns page of blacklisted personal ids"() {
        given:
        def pageResult = Mock Page

        when:
        def result = controller.getAll(page, size)

        then:
        1 * blacklistService.getAll(page, size) >> pageResult
        result == pageResult
    }
}
