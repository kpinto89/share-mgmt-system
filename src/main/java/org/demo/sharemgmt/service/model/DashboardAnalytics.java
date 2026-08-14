package org.demo.sharemgmt.service.model;

import java.util.List;

public class DashboardAnalytics {

    private final List<ChartBarItem> ownedSharesBySymbol;
    private final List<ChartBarItem> investmentByShareholder;
    private final List<ChartColumnItem> transactionActivity;

    public DashboardAnalytics(
        List<ChartBarItem> ownedSharesBySymbol,
        List<ChartBarItem> investmentByShareholder,
        List<ChartColumnItem> transactionActivity
    ) {
        this.ownedSharesBySymbol = ownedSharesBySymbol;
        this.investmentByShareholder = investmentByShareholder;
        this.transactionActivity = transactionActivity;
    }

    public List<ChartBarItem> getOwnedSharesBySymbol() {
        return ownedSharesBySymbol;
    }

    public List<ChartBarItem> getInvestmentByShareholder() {
        return investmentByShareholder;
    }

    public List<ChartColumnItem> getTransactionActivity() {
        return transactionActivity;
    }
}
