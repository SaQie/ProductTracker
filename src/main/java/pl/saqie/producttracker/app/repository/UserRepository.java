package pl.saqie.producttracker.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.saqie.producttracker.app.domain.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    Optional<User> findByActivationToken(String uuid);

    Optional<User> findByForgotPasswordToken(String uuid);

    Optional<User> findByEmail(String email);

    @Query("select u.id from User u where u.activationTokenExpiredDate <= :time")
    List<Long> tokenExpiredAccounts(LocalDateTime time);
}
