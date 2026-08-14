package org.demo.sharemgmt.web;

import org.demo.sharemgmt.service.PortfolioService;
import org.demo.sharemgmt.service.DashboardAnalyticsService;
import org.demo.sharemgmt.service.ShareholderService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

    private final ShareholderService shareholderService;
    private final PortfolioService portfolioService;
    private final DashboardAnalyticsService dashboardAnalyticsService;

    public DashboardController(
        ShareholderService shareholderService,
        PortfolioService portfolioService,
        DashboardAnalyticsService dashboardAnalyticsService
    ) {
        this.shareholderService = shareholderService;
        this.portfolioService = portfolioService;
        this.dashboardAnalyticsService = dashboardAnalyticsService;
    }

    @GetMapping("/")
    public String dashboard(Model model) {
        model.addAttribute("shareholderCount", shareholderService.getShareholderCount());
        model.addAttribute("transactionCount", portfolioService.getTransactionCount());
        model.addAttribute("ownedShareCount", portfolioService.getTotalOwnedShareCount());
        model.addAttribute("totalInvestment", portfolioService.getTotalInvestment());
        model.addAttribute("holdings", portfolioService.getHoldingSummaries());
        model.addAttribute("recentTransactions", portfolioService.getRecentTransactions());
        model.addAttribute("analytics", dashboardAnalyticsService.getDashboardAnalytics());
        return "dashboard";
    }
}
