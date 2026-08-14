package org.demo.sharemgmt.web;

import java.time.LocalDate;
import javax.validation.Valid;
import org.demo.sharemgmt.domain.TransactionType;
import org.demo.sharemgmt.service.ShareTransactionService;
import org.demo.sharemgmt.service.ShareholderService;
import org.demo.sharemgmt.web.form.ShareTransactionForm;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class TransactionController {

    private final ShareTransactionService shareTransactionService;
    private final ShareholderService shareholderService;

    public TransactionController(ShareTransactionService shareTransactionService, ShareholderService shareholderService) {
        this.shareTransactionService = shareTransactionService;
        this.shareholderService = shareholderService;
    }

    @ModelAttribute("transactionForm")
    public ShareTransactionForm transactionForm() {
        ShareTransactionForm form = new ShareTransactionForm();
        form.setTransactionType(TransactionType.BUY);
        form.setTransactionDate(LocalDate.now());
        return form;
    }

    @GetMapping("/transactions")
    public String transactions(Model model) {
        populateModel(model);
        return "transactions";
    }

    @PostMapping("/transactions")
    public String createTransaction(
        @Valid @ModelAttribute("transactionForm") ShareTransactionForm transactionForm,
        BindingResult bindingResult,
        Model model
    ) {
        if (bindingResult.hasErrors()) {
            populateModel(model);
            return "transactions";
        }
        try {
            shareTransactionService.recordTransaction(transactionForm);
            return "redirect:/transactions";
        } catch (IllegalArgumentException ex) {
            model.addAttribute("serviceError", ex.getMessage());
            populateModel(model);
            return "transactions";
        }
    }

    private void populateModel(Model model) {
        model.addAttribute("shareholders", shareholderService.getAllShareholders());
        model.addAttribute("transactions", shareTransactionService.getAllTransactions());
        model.addAttribute("transactionTypes", TransactionType.values());
    }
}
