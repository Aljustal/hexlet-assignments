package exercise.repository;

import exercise.model.User;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;


// BEGIN
public interface UserRepository extends ReactiveCrudRepository<User, Long> {
    Mono<User> findUserById(Long id);
}
// END
