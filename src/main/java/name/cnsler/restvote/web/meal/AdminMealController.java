package name.cnsler.restvote.web.meal;

import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import name.cnsler.restvote.error.IllegalRequestDataException;
import name.cnsler.restvote.model.Meal;
import name.cnsler.restvote.model.Restaurant;
import name.cnsler.restvote.repository.MealRepository;
import name.cnsler.restvote.repository.RestaurantRepository;
import name.cnsler.restvote.util.validation.ValidationUtil;
import org.springframework.data.domain.Example;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping(value = AdminMealController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
@Slf4j
public class AdminMealController {
    static final String REST_URL = "/api/admin/restaurants/{restaurantId}/meals";
    private final MealRepository mealRepository;
    private final RestaurantRepository restaurantRepository;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Meal> createWithLocation(@PathVariable int restaurantId, @Valid @RequestBody Meal meal) {
        log.info("restaurantId = {}", restaurantId);
        log.info("start request to DB");
        log.info("restaurantRepository.count() = {}", restaurantRepository.count());
        log.info("end request to DB");
        //TODO it's check restaurant id exists? Maybe BaseRepository.getExisted()?
        if (restaurantId < 1 || restaurantId > restaurantRepository.count()) {
            throw new IllegalRequestDataException("Restaurant with id=" + restaurantId + " not found");
        }
        meal.setRestaurant(restaurantRepository.getReferenceById(restaurantId));
//        meal.setRestaurant(restaurantRepository.getExisted(restaurantId));
        Meal created = mealRepository.save(meal);
        log.info("{} created", created);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(restaurantId, created.getId())
                .toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @GetMapping("/{id}")
    public Meal get(@PathVariable int restaurantId, @PathVariable int id) {
        //TODO check restaurant id exists?
        Optional<Meal> optionalMeal = mealRepository.getByIdAndRestaurantId(id, restaurantId);
        return optionalMeal.orElseThrow(
                () -> new IllegalRequestDataException(
                        "Meal with id=" + id + " for restaurant id=" + restaurantId + " not found"));
    }

    @PutMapping(value = "/{id}" ,consumes = MediaType.APPLICATION_JSON_VALUE)
    public void update(@PathVariable int restaurantId, @PathVariable int id, @Valid @RequestBody Meal meal) {
        //TODO check restaurant id exists?
//        ValidationUtil.assureIdConsistent(meal, id);
        Optional<Meal> optionalMeal = mealRepository.getByIdAndRestaurantId(id, restaurantId);
        Meal updatableMeal = optionalMeal.orElseThrow(
                () -> new IllegalRequestDataException(
                        "Meal with id=" + id + " for restaurant id=" + restaurantId + " not found"));
        log.info("updatableMeal={}", updatableMeal);
        meal.setId(id);
        Restaurant restaurant = updatableMeal.getRestaurant();
        meal.setRestaurant(restaurant);
        log.info("update {}", meal);
        mealRepository.save(meal);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable String restaurantId, @PathVariable int id) {
        //TODO check restaurant id exists
        //TODO check meal belong to restaurant
        log.info("delete meal with id={}", id);
        mealRepository.deleteExisted(id);
    }

    @GetMapping
    public List<Meal> getAll(@PathVariable int restaurantId) {
        log.info("1");
        Meal mealProbe = new Meal();
        log.info("2");
        mealProbe.setRestaurant(restaurantRepository.getExisted(restaurantId));
        log.info("3");
        Example<Meal> mealForRestaurant = Example.of(mealProbe);
        log.info("4");
        List<Meal> meals = mealRepository.findAll(mealForRestaurant);
        log.info("5");
        log.info("getAll meals {} for restaurant id={}", meals, restaurantId);
        return meals;
    }

    @GetMapping("/on-date")
    public List<Meal> getAllOnDate(@PathVariable int restaurantId, @Nullable LocalDate date) {
        LocalDate mealDate = Objects.requireNonNullElse(date, LocalDate.now());
        log.info("1");
        Meal mealProbe = new Meal();
        log.info("2");
        mealProbe.setRestaurant(restaurantRepository.getExisted(restaurantId));
        log.info("3");
        mealProbe.setMealDate(mealDate);
        log.info("4");
        List<Meal> meals = mealRepository.findAll(Example.of(mealProbe));
        log.info("getAll meals {} for restaurant id={} on date={}", meals, restaurantId, mealDate);
        return meals;
    }
}