package twino.integrations.geoip

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.util.concurrent.ListenableFuture
import org.springframework.web.client.AsyncRestOperations
import spock.lang.Specification

class CountryGeoipServiceImplSpec extends Specification {

    def geoIpUrl = "http://lalala/{ip}"
    def restTemplate = Mock AsyncRestOperations

    def service = new CountryGeoipServiceImpl(geoIpUrl, 'LV', restTemplate)

    def ip = "1.2.3.4"

    def "returns future with country code"() {
        given:
        def restResponse = Mock ListenableFuture
        def response = new FreeGeoIpNetResponse(countryCode: countryCode)
        restResponse.addCallback(_, _) >> { successCallback, errorCallback ->
            successCallback.onSuccess(new ResponseEntity(response, httpStatus))
        }
        restTemplate.getForEntity(geoIpUrl, FreeGeoIpNetResponse, ip) >> restResponse

        expect:
        service.getCountryBy(ip).join() == result

        where:
        httpStatus                       | countryCode || result
        HttpStatus.OK                    | 'LT'        || "LT"
        HttpStatus.OK                    | null        || "LV"
        HttpStatus.INTERNAL_SERVER_ERROR | 'LT'        || "LV"
    }
}
