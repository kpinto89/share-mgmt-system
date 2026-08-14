package org.demo.sharemgmt.service.model;

import java.util.List;

public class PortfolioView {

    private final PortfolioSummary summary;
    private final List<PortfolioPosition> positions;
    private final List<ChartBarItem> allocationBySymbol;
    private final List<ChartBarItem> allocationByShareholder;

    public PortfolioView(
        PortfolioSummary summary,
        List<PortfolioPosition> positions,
        List<ChartBarItem> allocationBySymbol,
        List<ChartBarItem> allocationByShareholder
    ) {
        this.summary = summary;
        this.positions = positions;
        this.allocationBySymbol = allocationBySymbol;
        this.allocationByShareholder = allocationByShareholder;
    }

    public PortfolioSummary getSummary() {
        return summary;
    }

    public List<PortfolioPosition> getPositions() {
        return positions;
    }

    public List<ChartBarItem> getAllocationBySymbol() {
        return allocationBySymbol;
    }

    public List<ChartBarItem> getAllocationByShareholder() {
        return allocationByShareholder;
    }
}
