package twino.infrastructure.limit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class CountryLimitServiceImpl implements CountryLimitService {

    private final long limitPerSecond;

    private final Map<String, AtomicLong> counters = new ConcurrentHashMap<>();

    public CountryLimitServiceImpl(@Value("${country.limit.service.limit}") long limitPerSecond) {
        this.limitPerSecond = limitPerSecond;
    }

    @Override
    public boolean isLimitReached(String countryCode) {
        AtomicLong counter = counters.computeIfAbsent(countryCode, k -> new AtomicLong());
        return counter.incrementAndGet() > limitPerSecond;
    }

    @Scheduled(fixedRate = 1000L)
    void reset() {
        counters.clear();
    }
}
