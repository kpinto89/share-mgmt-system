package org.demo.sharemgmt.strategy;

import org.demo.sharemgmt.domain.ShareTransaction;
import org.demo.sharemgmt.domain.Shareholder;
import org.demo.sharemgmt.domain.TransactionType;
import org.demo.sharemgmt.web.form.ShareTransactionForm;

public interface TransactionProcessor {

    TransactionType supports();

    ShareTransaction createTransaction(ShareTransactionForm form, Shareholder shareholder, String symbol, int currentHolding);
}
