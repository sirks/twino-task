package twino.integrations.geoip;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.client.AsyncRestOperations;

import java.util.concurrent.CompletableFuture;

import static com.google.common.base.Strings.isNullOrEmpty;
import static org.springframework.http.HttpStatus.OK;
import static twino.util.CompletableFutureHelper.listenableToCompletable;

@Service
public class CountryGeoipServiceImpl implements CountryGeoipService {

    private final String geoIpServiceUrlTemplate;
    private final String defaultCountryCode;
    private final AsyncRestOperations restTemplate;

    public CountryGeoipServiceImpl(@Value("${country.geoip.service.service.url.template}") String geoIpServiceUrlTemplate,
                                   @Value("${country.geoip.service.default.country.code}") String defaultCountryCode,
                                   AsyncRestOperations restTemplate) {
        this.geoIpServiceUrlTemplate = geoIpServiceUrlTemplate;
        this.defaultCountryCode = defaultCountryCode;
        this.restTemplate = restTemplate;
    }

    @Override
    public CompletableFuture<String> getCountryBy(String ip) {
        ListenableFuture<ResponseEntity<FreeGeoIpNetResponse>> future =
                restTemplate.getForEntity(geoIpServiceUrlTemplate, FreeGeoIpNetResponse.class, ip);
        return listenableToCompletable(future, responseEntity -> {
            if (responseEntity.getStatusCode() == OK) {
                String countryCode = responseEntity.getBody().getCountryCode();
                if (!isNullOrEmpty(countryCode)) {
                    return countryCode;
                }
            }
            return defaultCountryCode;
        });
    }
}
