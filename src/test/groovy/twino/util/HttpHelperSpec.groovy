package twino.util

import spock.lang.Specification

import javax.servlet.http.HttpServletRequest

class HttpHelperSpec extends Specification {

    def request = Mock HttpServletRequest

    def "returns ip from X-Forwarded-For header"() {
        given:
        request.getHeader("X-Forwarded-For") >> header
        request.getRemoteAddr() >> remoteAddr


        expect:
        HttpHelper.getClientIpAddress(request) == ip

        where:
        header                    | remoteAddr || ip
        null                      | '1.2.3.4'  || '1.2.3.4'
        ''                        | '1.2.3.4'  || '1.2.3.4'
        'unknown'                 | '1.2.3.4'  || '1.2.3.4'
        '2.3.4.5'                 | '1.2.3.4'  || '2.3.4.5'
        '2.3.4.5,6.7.8.9'         | '1.2.3.4'  || '2.3.4.5'
        '2.3.4.5,6.7.8.9,1.2.3.4' | '1.2.3.4'  || '2.3.4.5'
    }
}
