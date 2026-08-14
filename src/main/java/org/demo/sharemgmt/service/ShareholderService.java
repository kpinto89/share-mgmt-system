package org.demo.sharemgmt.service;

import java.util.List;
import org.demo.sharemgmt.domain.Shareholder;
import org.demo.sharemgmt.repository.ShareholderRepository;
import org.demo.sharemgmt.web.form.ShareholderForm;
import org.springframework.stereotype.Service;

@Service
public class ShareholderService {

    private final ShareholderRepository shareholderRepository;

    public ShareholderService(ShareholderRepository shareholderRepository) {
        this.shareholderRepository = shareholderRepository;
    }

    public List<Shareholder> getAllShareholders() {
        return shareholderRepository.findAllByOrderByNameAsc();
    }

    public Shareholder createShareholder(ShareholderForm form) {
        String email = form.getEmail().trim().toLowerCase();
        if (shareholderRepository.existsByEmailIgnoreCase(email)) {
            throw new IllegalArgumentException("A shareholder with this email already exists.");
        }
        Shareholder shareholder = new Shareholder(form.getName().trim(), email);
        return shareholderRepository.save(shareholder);
    }

    public long getShareholderCount() {
        return shareholderRepository.count();
    }
}
