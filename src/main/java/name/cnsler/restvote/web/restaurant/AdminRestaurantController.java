package name.cnsler.restvote.web.restaurant;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import name.cnsler.restvote.model.Restaurant;
import name.cnsler.restvote.repository.RestaurantRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(value = AdminRestaurantController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
@Slf4j
public class AdminRestaurantController {
    static final String REST_URL = "/api/admin/restaurants";
    private final RestaurantRepository restaurantRepository;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @CacheEvict(value = "restaurant", allEntries = true)
    public ResponseEntity<Restaurant> createWithLocation(@RequestBody @Valid Restaurant newRestaurant) {
        Restaurant created = restaurantRepository.save(newRestaurant);
        log.info("crate {}", newRestaurant);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(created.getId())
                .toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @GetMapping("/{id}")
    public Restaurant get(@PathVariable int id) {
        Restaurant restaurant = restaurantRepository.getExists(id, Restaurant.class);
        log.info("get {}", restaurant);
        return restaurant;
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Transactional
    @CacheEvict(value = "restaurant", allEntries = true)
    public void update(@PathVariable int id, @RequestBody @Valid Restaurant updatedRestaurant) {
        Restaurant restaurant = restaurantRepository.getExists(id, Restaurant.class);
        log.info("update {}", restaurant);
        log.info("updated {}", updatedRestaurant);
        updatedRestaurant.setId(id);
        restaurantRepository.save(updatedRestaurant);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @CacheEvict(value = "restaurant", allEntries = true)
    public void delete(@PathVariable int id) {
        restaurantRepository.deleteById(id);
        log.info("delete restaurant with id={}", id);
    }

    @GetMapping
    @Cacheable(value = "restaurant")
    public List<Restaurant> getAll() {
        List<Restaurant> restaurants = restaurantRepository.findAll();
        log.info("get all {}", restaurants);
        return restaurants;
    }
}