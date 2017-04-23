package twino.domain.model.loan;

import org.springframework.data.domain.Page;

public interface LoanService {

    Loan save(Loan loan);

    Page<Loan> getAll(int page, int size);

    Page<Loan> getByUserPersonalId(String personalId, int page, int size);
}
