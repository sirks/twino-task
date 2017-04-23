package twino.web.controller.loan;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class BlacklistedPersonalIdException extends RuntimeException {

    public BlacklistedPersonalIdException(String message) {
        super(message);
    }
}
