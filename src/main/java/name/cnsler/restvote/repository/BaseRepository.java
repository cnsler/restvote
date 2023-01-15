package name.cnsler.restvote.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.transaction.annotation.Transactional;

import static name.cnsler.restvote.util.validation.ValidationUtil.checkExists;
import static name.cnsler.restvote.util.validation.ValidationUtil.checkModification;

// https://stackoverflow.com/questions/42781264/multiple-base-repositories-in-spring-data-jpa
@NoRepositoryBean
public interface BaseRepository<T> extends JpaRepository<T, Integer> {

    //    https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#jpa.query.spel-expressions
    @Transactional
    @Modifying
    @Query("DELETE FROM #{#entityName} e WHERE e.id=:id")
    int delete(int id);

    default void deleteExists(int id, Class<T> clazz) {
        checkModification(delete(id), id, clazz);
    }

    @Query("SELECT e FROM #{#entityName} e WHERE e.id = :id")
    T get(int id);

    default T getExists(int id, Class<T> clazz) {
        return checkExists(get(id), id, clazz);
    }
}