package org.demo.sharemgmt.strategy;

import org.demo.sharemgmt.domain.ShareTransaction;
import org.demo.sharemgmt.domain.Shareholder;
import org.demo.sharemgmt.domain.TransactionType;
import org.demo.sharemgmt.web.form.ShareTransactionForm;
import org.springframework.stereotype.Component;

@Component
public class SellTransactionProcessor implements TransactionProcessor {

    @Override
    public TransactionType supports() {
        return TransactionType.SELL;
    }

    @Override
    public ShareTransaction createTransaction(ShareTransactionForm form, Shareholder shareholder, String symbol, int currentHolding) {
        if (currentHolding < form.getQuantity()) {
            throw new IllegalArgumentException(
                "Cannot sell " + form.getQuantity() + " shares of " + symbol + " because only " + currentHolding + " are currently owned."
            );
        }
        return new ShareTransaction(
            shareholder,
            symbol,
            TransactionType.SELL,
            form.getQuantity(),
            form.getPricePerShare(),
            form.getTransactionDate()
        );
    }
}
