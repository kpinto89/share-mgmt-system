package org.demo.sharemgmt.config;

import java.math.BigDecimal;
import java.time.LocalDate;
import org.demo.sharemgmt.domain.UserRole;
import org.demo.sharemgmt.domain.ShareTransaction;
import org.demo.sharemgmt.domain.Shareholder;
import org.demo.sharemgmt.domain.TransactionType;
import org.demo.sharemgmt.service.AppUserService;
import org.demo.sharemgmt.repository.ShareTransactionRepository;
import org.demo.sharemgmt.repository.ShareholderRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataSeeder {

    @Bean
    CommandLineRunner seedSampleData(
        AppUserService appUserService,
        ShareholderRepository shareholderRepository,
        ShareTransactionRepository shareTransactionRepository
    ) {
        return args -> {
            try {
                appUserService.createUser("admin", "admin123", "System Administrator", UserRole.ADMIN);
            } catch (IllegalArgumentException ignored) {
                // User already exists in the current persistence context.
            }

            if (shareholderRepository.count() > 0) {
                return;
            }

            Shareholder anika = shareholderRepository.save(new Shareholder("Anika Sharma", "anika@investor.local"));
            Shareholder ravi = shareholderRepository.save(new Shareholder("Ravi Menon", "ravi@investor.local"));

            shareTransactionRepository.save(
                new ShareTransaction(anika, "TCS", TransactionType.BUY, 25, new BigDecimal("3650.00"), LocalDate.now().minusDays(10))
            );
            shareTransactionRepository.save(
                new ShareTransaction(anika, "INFY", TransactionType.BUY, 18, new BigDecimal("1485.50"), LocalDate.now().minusDays(8))
            );
            shareTransactionRepository.save(
                new ShareTransaction(ravi, "RELIANCE", TransactionType.BUY, 12, new BigDecimal("2870.00"), LocalDate.now().minusDays(6))
            );
            shareTransactionRepository.save(
                new ShareTransaction(anika, "TCS", TransactionType.SELL, 5, new BigDecimal("3725.00"), LocalDate.now().minusDays(2))
            );
        };
    }
}
