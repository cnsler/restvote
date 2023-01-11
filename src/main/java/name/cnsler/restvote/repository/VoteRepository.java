package name.cnsler.restvote.repository;

import name.cnsler.restvote.model.Vote;
import org.springframework.stereotype.Repository;

@Repository
public interface VoteRepository extends BaseRepository<Vote> {
}