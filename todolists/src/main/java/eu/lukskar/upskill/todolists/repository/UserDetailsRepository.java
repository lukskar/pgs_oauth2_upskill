package eu.lukskar.upskill.todolists.repository;

import eu.lukskar.upskill.todolists.model.UserDetails;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserDetailsRepository extends MongoRepository<UserDetails, String> {
}
