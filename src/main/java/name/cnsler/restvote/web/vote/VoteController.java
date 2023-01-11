package name.cnsler.restvote.web.vote;

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
        //TODO check exist vote
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

    //TODO get all votes on date?
    @GetMapping
    public List<Vote> getAll() {
        List<Vote> votes = voteRepository.findAllByVoteDate(LocalDate.now());
        log.info("getAll votes={}", votes);
        //TODO restaurant id instead Restaurant{id, title}?
        return votes;
    }
}