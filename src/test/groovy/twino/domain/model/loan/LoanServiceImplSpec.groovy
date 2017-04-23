package twino.domain.model.loan

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import spock.lang.Specification

class LoanServiceImplSpec extends Specification {

    def loanRepository = Mock LoanRepository

    def service = new LoanServiceImpl(loanRepository)

    def loan = new Loan()
    def page = 11
    def size = 22
    def personalId = "persid"

    def "saves the loan"() {
        given:
        def savedLoan = new Loan()

        when:
        def result = service.save(loan)

        then:
        1 * loanRepository.save(loan) >> savedLoan
        result == savedLoan
    }

    def "returns page of loans"() {
        given:
        def pageResult = Mock Page

        when:
        def result = service.getAll(page, size)

        then:
        1 * loanRepository.findAll({ it.pageNumber == page && it.pageSize == size } as Pageable) >> pageResult
        result == pageResult
    }

    def "returns page of loans for personal id"() {
        given:
        def pageResult = Mock Page

        when:
        def result = service.getByUserPersonalId(personalId, page, size)

        then:
        1 * loanRepository.findByPersonalId(personalId, {
            it.pageNumber == page && it.pageSize == size
        } as Pageable) >> pageResult
        result == pageResult
    }
}
