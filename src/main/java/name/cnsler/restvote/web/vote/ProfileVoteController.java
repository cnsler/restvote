package name.cnsler.restvote.web.vote;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import name.cnsler.restvote.error.IllegalRequestDataException;
import name.cnsler.restvote.model.Vote;
import name.cnsler.restvote.repository.RestaurantRepository;
import name.cnsler.restvote.repository.VoteRepository;
import name.cnsler.restvote.to.VoteTo;
import name.cnsler.restvote.web.AuthUser;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping(value = ProfileVoteController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
@Slf4j
public class ProfileVoteController {
    static final String REST_URL = "/api/profile/votes";
    private final VoteRepository voteRepository;
    private final RestaurantRepository restaurantRepository;
    private final LocalTime voteEndTime = LocalTime.parse("12:00");

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Vote> createWithLocation(@AuthenticationPrincipal AuthUser authUser, @Valid @RequestBody VoteTo voteTo) {
        int restaurantId = voteTo.getRestaurantId();
        Vote vote = new Vote();
        vote.setUser(authUser.getUser());
        vote.setRestaurant(restaurantRepository.getReferenceById(restaurantId));
        vote.setVoteDate(LocalDate.now());
        log.info("{}", vote);
        checkVoteEndTime(vote);
        Vote created = voteRepository.save(vote);
        log.info("create {}", created);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(created.getId())
                .toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @GetMapping("/{id}")
    public Vote get(@AuthenticationPrincipal AuthUser authUser, @PathVariable int id) {
        Vote vote = voteExists(id, authUser.id());
        log.info("get {}", vote);
        return vote;
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@AuthenticationPrincipal AuthUser authUser, @PathVariable int id, @Valid @RequestBody VoteTo voteTo) {
        int restaurantId = voteTo.getRestaurantId();
        Vote vote = voteExists(id, authUser.id());
        if (restaurantId == vote.getRestaurant().id()) return;
        vote.setRestaurant(restaurantRepository.getReferenceById(restaurantId));
        checkVoteEndTime(vote);
        Vote updated = voteRepository.save(vote);
        log.info("updated {}", updated);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@AuthenticationPrincipal AuthUser authUser, @PathVariable int id) {
        Vote vote = voteExists(id, authUser.id());
        checkVoteEndTime(vote);
        voteRepository.delete(vote);
        log.info("delete vote with id={}", id);
    }

    @GetMapping
    public List<Vote> getAll(@AuthenticationPrincipal AuthUser authUser) {
        List<Vote> votes = voteRepository.findAllByUserIdOrderByVoteDateDesc(authUser.id());
        log.info("getAll votes={}", votes);
        //TODO restaurant id instead Restaurant{id, title}:
        // VoteTo{id, restaurantId, date(?)}?
        return votes;
    }

    private Vote voteExists(int id, int userId) {
        return voteRepository.getByIdAndUserId(id, userId).orElseThrow(
                () -> new IllegalRequestDataException("Vote with id=" + id + " not found"));
    }

    private void checkVoteEndTime(Vote vote) {
        if (vote.getVoteDate().isBefore(LocalDate.now())) {
            throw new IllegalRequestDataException("You can't change old votes");
        }
        if (!LocalTime.now().isBefore(voteEndTime)) {
            throw new IllegalRequestDataException("Too late to vote");
        }
    }
}