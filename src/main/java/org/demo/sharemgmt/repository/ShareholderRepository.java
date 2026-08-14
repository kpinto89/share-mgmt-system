package org.demo.sharemgmt.repository;

import java.util.List;
import org.demo.sharemgmt.domain.Shareholder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShareholderRepository extends JpaRepository<Shareholder, Long> {

    List<Shareholder> findAllByOrderByNameAsc();

    boolean existsByEmailIgnoreCase(String email);
}
