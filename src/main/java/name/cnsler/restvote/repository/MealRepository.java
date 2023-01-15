package name.cnsler.restvote.repository;

import name.cnsler.restvote.model.Meal;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional(readOnly = true)
public interface MealRepository extends BaseRepository<Meal> {

    List<Meal> findAllByRestaurantId(int restaurantId, Sort sort);

    List<Meal> findAllByRestaurantIdAndDate(int restaurantId, LocalDate date);

    Optional<Meal> getByIdAndRestaurantId(int id, int restaurantId);
}