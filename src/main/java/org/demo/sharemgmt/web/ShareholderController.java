package org.demo.sharemgmt.web;

import javax.validation.Valid;
import org.demo.sharemgmt.service.ShareholderService;
import org.demo.sharemgmt.web.form.ShareholderForm;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class ShareholderController {

    private final ShareholderService shareholderService;

    public ShareholderController(ShareholderService shareholderService) {
        this.shareholderService = shareholderService;
    }

    @ModelAttribute("shareholderForm")
    public ShareholderForm shareholderForm() {
        return new ShareholderForm();
    }

    @GetMapping("/shareholders")
    public String shareholders(Model model) {
        model.addAttribute("shareholders", shareholderService.getAllShareholders());
        return "shareholders";
    }

    @PostMapping("/shareholders")
    public String createShareholder(
        @Valid @ModelAttribute("shareholderForm") ShareholderForm shareholderForm,
        BindingResult bindingResult,
        Model model
    ) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("shareholders", shareholderService.getAllShareholders());
            return "shareholders";
        }
        try {
            shareholderService.createShareholder(shareholderForm);
            return "redirect:/shareholders";
        } catch (IllegalArgumentException ex) {
            model.addAttribute("serviceError", ex.getMessage());
            model.addAttribute("shareholders", shareholderService.getAllShareholders());
            return "shareholders";
        }
    }
}
