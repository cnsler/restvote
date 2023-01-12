package name.cnsler.restvote.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"restaurant_id", "meal_date", "name"},
        name = "meal_unique_restaurant_date_name")})
@Getter
@Setter
public class Meal extends NamedEntity {
    @Column(name = "price", nullable = false)
    private Integer price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", nullable = false)
    @JsonIgnore
    private Restaurant restaurant;

    @Column(name = "meal_date", nullable = false)
    private LocalDate mealDate;

    @Override
    public String toString() {
        return "Meal{" +
                "id=" + this.getId() +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", restaurantId=" + restaurant.getId() +
                ", mealDate=" + mealDate +
                '}';
    }
}