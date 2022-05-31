package eu.lukskar.upskill.todolists.repository;

import eu.lukskar.upskill.todolists.model.DbUserDetails;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserDetailsRepository extends MongoRepository<DbUserDetails, String> {
    DbUserDetails findByUsername(String username);
}
