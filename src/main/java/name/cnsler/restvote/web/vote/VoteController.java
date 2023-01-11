package name.cnsler.restvote.web.vote;

import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import name.cnsler.restvote.model.Vote;
import name.cnsler.restvote.repository.RestaurantRepository;
import name.cnsler.restvote.repository.VoteRepository;
import name.cnsler.restvote.web.AuthUser;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
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
    private final RestaurantRepository restaurantRepository;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Vote> createWithLocation(@AuthenticationPrincipal AuthUser authUser, @RequestBody Integer restaurantId) {
        //TODO check restaurant id exists
        //TODO check user vote exists
        //TODO update vote in POST method?
        Vote vote = new Vote();
        vote.setUser(authUser.getUser());
        vote.setRestaurant(restaurantRepository.getReferenceById(restaurantId));
        vote.setVoteDate(LocalDate.now());
        //TODO end time constraint
        Vote created = voteRepository.save(vote);
        log.info("create {}", created);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(created.getId())
                .toUri();
        //TODO restaurant id instead of null in response body
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    //TODO get vote?

    //TODO update vote in PUT method?

    //TODO delete vote?

    //TODO get all votes for current user?

    @GetMapping
    public List<Vote> getAll(@Nullable LocalDate date) {
        LocalDate voteDate = Objects.requireNonNullElse(date, LocalDate.now());
        List<Vote> votes = voteRepository.findAllByVoteDate(voteDate);
        log.info("getAll votes={} on date={}", votes, voteDate);
        //TODO restaurant id instead Restaurant{id, title}?
        return votes;
    }
}