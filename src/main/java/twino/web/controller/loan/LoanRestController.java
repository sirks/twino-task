package twino.web.controller.loan;

import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;
import twino.domain.model.blacklist.BlacklistService;
import twino.domain.model.loan.Loan;
import twino.domain.model.loan.LoanService;
import twino.infrastructure.limit.CountryLimitService;
import twino.integrations.geoip.CountryGeoipService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import static twino.util.CompletableFutureHelper.completableToDeferred;
import static twino.util.HttpHelper.getClientIpAddress;

@RestController
@RequestMapping("/loan")
public class LoanRestController {

    private static final String DEFAULT_PAGE_NUM = "0";
    private static final String DEFAULT_PAGE_SIZE = "20";

    private final LoanService loanService;
    private final CountryGeoipService countryGeoipService;
    private final BlacklistService blacklistService;
    private final CountryLimitService countryLimitService;

    public LoanRestController(LoanService loanService, CountryGeoipService countryGeoipService,
                              BlacklistService blacklistService, CountryLimitService countryLimitService) {
        this.loanService = loanService;
        this.countryGeoipService = countryGeoipService;
        this.blacklistService = blacklistService;
        this.countryLimitService = countryLimitService;
    }

    @PutMapping(consumes = {"application/json"})
    public DeferredResult<Loan> applyForLoan(@RequestBody @Valid Loan loan, HttpServletRequest request) {
        return completableToDeferred(countryGeoipService.getCountryBy(getClientIpAddress(request))
                .thenApply(countryCode -> {
                    if (blacklistService.isBlacklisted(loan.getPersonalId())) {
                        throw new BlacklistedPersonalIdException("PersonalId [" + loan.getPersonalId() +
                                "] is blacklisted");
                    }
                    if (countryLimitService.isLimitReached(countryCode)) {
                        throw new LimitExceededException("Limit exceeded");
                    }
                    loan.setCountryCode(countryCode);
                    return loanService.save(loan);
                }));
    }

    @GetMapping(produces = {"application/json"})
    public Page<Loan> getAll(@RequestParam(value = "page", defaultValue = DEFAULT_PAGE_NUM) int page,
                             @RequestParam(value = "size", defaultValue = DEFAULT_PAGE_SIZE) int size) {
        return loanService.getAll(page, size);
    }

    @GetMapping(value = "/{personalId}", produces = {"application/json"})
    public Page<Loan> getByUserPersonalId(@PathVariable("personalId") String personalId,
                                          @RequestParam(value = "page", defaultValue = DEFAULT_PAGE_NUM) int page,
                                          @RequestParam(value = "size", defaultValue = DEFAULT_PAGE_SIZE) int size) {
        return loanService.getByUserPersonalId(personalId, page, size);
    }
}
