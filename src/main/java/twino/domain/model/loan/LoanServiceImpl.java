package twino.domain.model.loan;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class LoanServiceImpl implements LoanService {

    private final LoanRepository loanRepository;

    public LoanServiceImpl(LoanRepository loanRepository) {
        this.loanRepository = loanRepository;
    }

    @Override
    public Loan save(Loan loan) {
        return loanRepository.save(loan);
    }

    @Override
    public Page<Loan> getAll(int page, int size) {
        return loanRepository.findAll(new PageRequest(page, size));
    }

    @Override
    public Page<Loan> getByUserPersonalId(String personalId, int page, int size) {
        return loanRepository.findByPersonalId(personalId, new PageRequest(page, size));
    }
}
