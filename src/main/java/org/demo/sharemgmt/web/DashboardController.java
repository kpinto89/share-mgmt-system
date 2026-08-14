package org.demo.sharemgmt.web;

import org.demo.sharemgmt.service.PortfolioService;
import org.demo.sharemgmt.service.ShareholderService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

    private final ShareholderService shareholderService;
    private final PortfolioService portfolioService;

    public DashboardController(ShareholderService shareholderService, PortfolioService portfolioService) {
        this.shareholderService = shareholderService;
        this.portfolioService = portfolioService;
    }

    @GetMapping("/")
    public String dashboard(Model model) {
        model.addAttribute("shareholderCount", shareholderService.getShareholderCount());
        model.addAttribute("transactionCount", portfolioService.getTransactionCount());
        model.addAttribute("ownedShareCount", portfolioService.getTotalOwnedShareCount());
        model.addAttribute("totalInvestment", portfolioService.getTotalInvestment());
        model.addAttribute("holdings", portfolioService.getHoldingSummaries());
        model.addAttribute("recentTransactions", portfolioService.getRecentTransactions());
        return "dashboard";
    }
}
