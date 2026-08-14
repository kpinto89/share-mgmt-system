package org.demo.sharemgmt.repository;

import java.util.List;
import org.demo.sharemgmt.domain.ShareTransaction;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShareTransactionRepository extends JpaRepository<ShareTransaction, Long> {

    @EntityGraph(attributePaths = "shareholder")
    List<ShareTransaction> findAllByOrderByTransactionDateDescIdDesc();

    @EntityGraph(attributePaths = "shareholder")
    List<ShareTransaction> findTop10ByOrderByTransactionDateDescIdDesc();

    @EntityGraph(attributePaths = "shareholder")
    List<ShareTransaction> findByShareholderIdOrderByTransactionDateAscIdAsc(Long shareholderId);

    @EntityGraph(attributePaths = "shareholder")
    List<ShareTransaction> findByShareholderIdAndSymbolIgnoreCaseOrderByTransactionDateAscIdAsc(Long shareholderId, String symbol);

    @EntityGraph(attributePaths = "shareholder")
    ShareTransaction findFirstBySymbolIgnoreCaseOrderByTransactionDateDescIdDesc(String symbol);
}
