package eu.lukskar.upskill.todolists.repository;

import eu.lukskar.upskill.todolists.model.DbUserDetails;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserDetailsRepository extends MongoRepository<DbUserDetails, String> {
    Optional<DbUserDetails> findByUsername(String username);
    Optional<DbUserDetails> findByEmail(String email);
}
