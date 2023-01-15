package name.cnsler.restvote.repository;

import name.cnsler.restvote.model.Vote;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface VoteRepository extends BaseRepository<Vote> {

    @EntityGraph(attributePaths = "restaurant")
    Optional<Vote> getByIdAndUserId(int id, int userId);

    @EntityGraph(attributePaths = "restaurant")
    List<Vote> findAllByUserIdOrderByDateDesc(int userId);

    @EntityGraph(attributePaths = "restaurant")
    List<Vote> findAllByDate(LocalDate date);

    @EntityGraph(attributePaths = "restaurant")
    @Query("SELECT v FROM Vote v ORDER BY v.date DESC")
    List<Vote> findAllSorted();
}