package name.cnsler.restvote.web.vote;

import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import name.cnsler.restvote.model.Vote;
import name.cnsler.restvote.repository.VoteRepository;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping(value = VoteController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
@Slf4j
public class VoteController {
    static final String REST_URL = "/api/votes";
    private final VoteRepository voteRepository;

    @GetMapping
    public List<Vote> getAll() {
        List<Vote> votes = voteRepository.findAllSorted();
        log.info("getAll votes={}", votes);
        return votes;
    }

    @GetMapping("/on-date")
    public List<Vote> getAllOnDate(@Nullable LocalDate date) {
        LocalDate voteDate = Objects.requireNonNullElse(date, LocalDate.now());
        List<Vote> votes = voteRepository.findAllByVoteDate(voteDate);
        log.info("getAll votes={} on date={}", votes, voteDate);
        return votes;
    }
}