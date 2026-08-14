package org.demo.sharemgmt.web;

import org.demo.sharemgmt.service.PortfolioService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PortfolioController {

    private final PortfolioService portfolioService;

    public PortfolioController(PortfolioService portfolioService) {
        this.portfolioService = portfolioService;
    }

    @GetMapping("/portfolio")
    public String portfolio(Model model) {
        model.addAttribute("portfolio", portfolioService.getPortfolioView());
        return "portfolio";
    }
}
