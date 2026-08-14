package org.demo.sharemgmt.strategy;

import org.demo.sharemgmt.domain.ShareTransaction;
import org.demo.sharemgmt.domain.Shareholder;
import org.demo.sharemgmt.domain.TransactionType;
import org.demo.sharemgmt.web.form.ShareTransactionForm;
import org.springframework.stereotype.Component;

@Component
public class BuyTransactionProcessor implements TransactionProcessor {

    @Override
    public TransactionType supports() {
        return TransactionType.BUY;
    }

    @Override
    public ShareTransaction createTransaction(ShareTransactionForm form, Shareholder shareholder, String symbol, int currentHolding) {
        return new ShareTransaction(
            shareholder,
            symbol,
            TransactionType.BUY,
            form.getQuantity(),
            form.getPricePerShare(),
            form.getTransactionDate()
        );
    }
}
