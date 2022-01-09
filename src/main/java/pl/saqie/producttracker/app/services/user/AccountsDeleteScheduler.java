package pl.saqie.producttracker.app.services.user;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import pl.saqie.producttracker.app.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class AccountsDeleteScheduler {

    private final UserRepository repository;
    private final Logger logger = LoggerFactory.getLogger(AccountsDeleteScheduler.class);

    @Scheduled(cron = "0 0 * * * ?", zone = "Europe/Warsaw")
    public void deleteExpiredAccounts() {
        List<Long> accountDtoList = repository.tokenExpiredAccounts(LocalDateTime.now());
        removeAccounts(accountDtoList);
    }

    private void removeAccounts(List<Long> accountDtoList) {
        repository.deleteAllById(accountDtoList);
        logger.info("Usunięto konta nieaktywnych użytkowników (" + accountDtoList.size() + ")");
    }
}
