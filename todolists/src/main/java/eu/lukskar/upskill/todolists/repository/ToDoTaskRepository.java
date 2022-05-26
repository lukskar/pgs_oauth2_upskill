package eu.lukskar.upskill.todolists.repository;

import eu.lukskar.upskill.todolists.model.ToDoTask;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ToDoTaskRepository extends MongoRepository<ToDoTask, String> {
    Optional<ToDoTask> findByIdAndUserId(String id, String userId);
    List<ToDoTask> findAllByUserId(String userId);
    void deleteByIdAndUserId(String id, String userId);
}
