package twino.integrations.geoip;

import java.util.concurrent.CompletableFuture;

public interface CountryGeoipService {

    CompletableFuture<String> getCountryBy(String ip);
}
