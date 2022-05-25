package eu.lukskar.upskill.todolists.repository;

import eu.lukskar.upskill.todolists.model.ToDoTask;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ToDoTaskRepository extends MongoRepository<ToDoTask, String> {
}
