package eu.lukskar.upskill.jwttimeapi.repository;

import eu.lukskar.upskill.jwttimeapi.model.UserDetails;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserDetailsRepository extends MongoRepository<UserDetails, String> {
}
