package exercise.repository;

import exercise.model.City;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface CityRepository extends CrudRepository<City, Long> {

    // BEGIN
    List<City> findByNameStartingWithIgnoreCase(String prefix);
    List<City> findAllByOrderByNameAsc();
    // END
}
