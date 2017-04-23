package twino.web.controller.blacklist;

import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import twino.domain.model.blacklist.BlacklistItem;
import twino.domain.model.blacklist.BlacklistService;

@RestController
@RequestMapping("/blacklist")
public class BlacklistRestController {

    private static final String DEFAULT_PAGE_NUM = "0";
    private static final String DEFAULT_PAGE_SIZE = "20";

    private final BlacklistService blacklistService;

    public BlacklistRestController(BlacklistService blacklistService) {
        this.blacklistService = blacklistService;
    }

    @PutMapping
    public void addToBlacklist(@RequestParam("personalId") String personalId) {
        blacklistService.save(personalId);
    }

    @GetMapping(produces = {"application/json"})
    public Page<BlacklistItem> getAll(@RequestParam(value = "page", defaultValue = DEFAULT_PAGE_NUM) int page,
                                      @RequestParam(value = "size", defaultValue = DEFAULT_PAGE_SIZE) int size) {
        return blacklistService.getAll(page, size);
    }

}