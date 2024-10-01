package live.mukeshtechlab.userservice.repositories;

import live.mukeshtechlab.userservice.models.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<Token, Long> {
    /*
    SELECT * FROM token
    WHERE value = tokenValue AND
    idDeleted = isDeleted AND
    expiryAt > currentDate;
     */

    Optional<Token> findByValueAndDeletedAndExpiryAtGreaterThan(
            String token,
            boolean deleted,
            Date currentTime
    );
}
