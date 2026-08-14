package org.demo.sharemgmt.service;

import java.util.List;
import org.demo.sharemgmt.domain.ShareTransaction;
import org.demo.sharemgmt.domain.Shareholder;
import org.demo.sharemgmt.repository.ShareTransactionRepository;
import org.demo.sharemgmt.repository.ShareholderRepository;
import org.demo.sharemgmt.strategy.TransactionProcessor;
import org.demo.sharemgmt.strategy.TransactionProcessorFactory;
import org.demo.sharemgmt.web.form.ShareTransactionForm;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ShareTransactionService {

    private final ShareholderRepository shareholderRepository;
    private final ShareTransactionRepository shareTransactionRepository;
    private final PortfolioService portfolioService;
    private final TransactionProcessorFactory transactionProcessorFactory;

    public ShareTransactionService(
        ShareholderRepository shareholderRepository,
        ShareTransactionRepository shareTransactionRepository,
        PortfolioService portfolioService,
        TransactionProcessorFactory transactionProcessorFactory
    ) {
        this.shareholderRepository = shareholderRepository;
        this.shareTransactionRepository = shareTransactionRepository;
        this.portfolioService = portfolioService;
        this.transactionProcessorFactory = transactionProcessorFactory;
    }

    public List<ShareTransaction> getAllTransactions() {
        return shareTransactionRepository.findAllByOrderByTransactionDateDescIdDesc();
    }

    @Transactional
    public ShareTransaction recordTransaction(ShareTransactionForm form) {
        Shareholder shareholder = shareholderRepository.findById(form.getShareholderId())
            .orElseThrow(() -> new IllegalArgumentException("Selected shareholder does not exist."));
        String symbol = form.getSymbol().trim().toUpperCase();
        int currentHolding = portfolioService.getCurrentHoldingQuantity(shareholder.getId(), symbol);
        TransactionProcessor processor = transactionProcessorFactory.getProcessor(form.getTransactionType());
        ShareTransaction transaction = processor.createTransaction(form, shareholder, symbol, currentHolding);
        return shareTransactionRepository.save(transaction);
    }
}
