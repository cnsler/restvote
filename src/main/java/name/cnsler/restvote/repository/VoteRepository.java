package name.cnsler.restvote.repository;

import name.cnsler.restvote.model.Vote;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface VoteRepository extends BaseRepository<Vote> {

    @EntityGraph(attributePaths = "restaurant")
    List<Vote> findAllByVoteDate(LocalDate voteDate);
}