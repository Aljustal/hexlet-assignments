package exercise.repository;

import exercise.model.User;
// import org.springframework.data.repository.CrudRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import com.querydsl.core.types.dsl.StringPath;
import exercise.model.QUser;


import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends
    JpaRepository<User, Long>,
    QuerydslPredicateExecutor<User>,
    QuerydslBinderCustomizer<QUser> {

    @Override
    default void customize(QuerydslBindings bindings, QUser user) {
        // Дополнительная задача

        // BEGIN
        
        // END
    }

}
