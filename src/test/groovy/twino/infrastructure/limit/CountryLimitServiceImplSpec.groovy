package twino.infrastructure.limit

import spock.lang.Specification

class CountryLimitServiceImplSpec extends Specification {

    def limitPerSecond = 2

    def service = new CountryLimitServiceImpl(limitPerSecond)

    def "returns true if limit is reached for country"() {
        when:
        def result1 = service.isLimitReached("LV")
        def result2 = service.isLimitReached("LV")
        def result3 = service.isLimitReached("LV")
        def result4 = service.isLimitReached("LT")

        then:
        !result1
        !result2
        result3
        !result4
    }

    def "resets the counters"() {
        when:
        def result1 = service.isLimitReached("LV")
        def result2 = service.isLimitReached("LV")
        def result3 = service.isLimitReached("LV")
        service.reset()
        def result4 = service.isLimitReached("LV")

        then:
        !result1
        !result2
        result3
        !result4
    }
}
