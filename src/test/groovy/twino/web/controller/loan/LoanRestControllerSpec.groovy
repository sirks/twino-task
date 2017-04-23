package twino.web.controller.loan

import org.springframework.data.domain.Page
import twino.domain.model.blacklist.BlacklistService
import twino.domain.model.loan.Loan
import twino.domain.model.loan.LoanService
import twino.infrastructure.limit.CountryLimitService
import twino.integrations.geoip.CountryGeoipService
import spock.lang.Specification

import javax.servlet.http.HttpServletRequest
import java.util.concurrent.CompletableFuture

class LoanRestControllerSpec extends Specification {

    def loanService = Mock LoanService
    def countryService = Mock CountryGeoipService
    def blacklistService = Mock BlacklistService
    def countryLimitService = Mock CountryLimitService

    def controller = new LoanRestController(loanService, countryService, blacklistService, countryLimitService)

    def httpRequest = Mock HttpServletRequest
    def ip = "1.2.3.4"
    def countryCode = "LV"
    def personalId = "persid"
    def loan = new Loan(personalId: personalId)
    def page = 11
    def size = 22

    def "saves and returns the loan"() {
        given:
        httpRequest.getRemoteAddr() >> ip
        countryService.getCountryBy(ip) >> CompletableFuture.completedFuture(countryCode)
        blacklistService.isBlacklisted(personalId) >> false
        countryLimitService.isLimitReached(countryCode) >> false
        def savedLoan = new Loan()

        when:
        def result = controller.applyForLoan(loan, httpRequest)

        then:
        1 * loanService.save(loan) >> savedLoan
        loan.countryCode == countryCode
        result.getResult() == savedLoan
    }

    def "fails saving with exception if personal id is blacklisted"() {
        given:
        httpRequest.getRemoteAddr() >> ip
        countryService.getCountryBy(ip) >> CompletableFuture.completedFuture(countryCode)
        blacklistService.isBlacklisted(personalId) >> true

        when:
        def result = controller.applyForLoan(loan, httpRequest)

        then:
        0 * loanService.save(_)
        result.getResult() instanceof BlacklistedPersonalIdException
    }

    def "fails saving with exception if limit by country is reached"() {
        given:
        httpRequest.getRemoteAddr() >> ip
        countryService.getCountryBy(ip) >> CompletableFuture.completedFuture(countryCode)
        blacklistService.isBlacklisted(personalId) >> false
        countryLimitService.isLimitReached(countryCode) >> true

        when:
        def result = controller.applyForLoan(loan, httpRequest)

        then:
        0 * loanService.save(_)
        result.getResult() instanceof LimitExceededException
    }

    def "returns page of loans"() {
        given:
        def pageResult = Mock Page

        when:
        def result = controller.getAll(page, size)

        then:
        1 * loanService.getAll(page, size) >> pageResult
        result == pageResult
    }

    def "returns page of loans by personal id"() {
        given:
        def pageResult = Mock Page

        when:
        def result = controller.getByUserPersonalId(personalId, page, size)

        then:
        1 * loanService.getByUserPersonalId(personalId, page, size) >> pageResult
        result == pageResult
    }
}
