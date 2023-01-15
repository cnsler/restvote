package name.cnsler.restvote.web.vote;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import name.cnsler.restvote.error.IllegalRequestDataException;
import name.cnsler.restvote.model.Restaurant;
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
import java.util.List;

@RestController
@RequestMapping(value = ProfileVoteController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
@Slf4j
public class ProfileVoteController {
    static final String REST_URL = "/api/profile/votes";
    private final VoteRepository voteRepository;
    private final RestaurantRepository restaurantRepository;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Vote> createWithLocation(@AuthenticationPrincipal AuthUser authUser, @Valid @RequestBody VoteTo voteTo) {
        int restaurantId = voteTo.getRestaurantId();
        Restaurant restaurant = restaurantRepository.findById(restaurantId).orElseThrow(
                () -> new IllegalRequestDataException("Restaurant with id=" + restaurantId + " not found"));
        log.info("get {}", restaurant);
        Vote vote = new Vote();
        vote.setUser(authUser.getUser());
        vote.setRestaurant(restaurant);
        vote.setVoteDate(LocalDate.now());
        //TODO end time constraint
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
        Vote vote = voteRepository.getByIdAndUserId(id, authUser.id()).orElseThrow(
                        () -> new IllegalRequestDataException("Vote with id=" + id + " not found"));
        log.info("get {}", vote);
        return vote;
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@AuthenticationPrincipal AuthUser authUser, @PathVariable int id, @Valid @RequestBody VoteTo voteTo) {
        int restaurantId = voteTo.getRestaurantId();

        Restaurant restaurant = restaurantRepository.findById(restaurantId).orElseThrow(
                () -> new IllegalRequestDataException("Restaurant with id=" + restaurantId + " not found"));
        log.info("get {}", restaurant);

        Vote vote = voteRepository.getByIdAndUserId(id, authUser.id()).orElseThrow(
                () -> new IllegalRequestDataException("Vote with id=" + id + " not found"));

        if (restaurantId == vote.getRestaurant().id()) return;
        vote.setRestaurant(restaurant);
        //TODO end time constraint
        Vote updated = voteRepository.save(vote);
        log.info("updated {}", updated);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@AuthenticationPrincipal AuthUser authUser, @PathVariable int id) {
        Vote vote = voteRepository.getByIdAndUserId(id, authUser.id()).orElseThrow(
                () -> new IllegalRequestDataException("Vote with id=" + id + " not found"));
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
}