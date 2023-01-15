package name.cnsler.restvote.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDate;

@Entity
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"restaurant_id", "meal_date", "name"},
        name = "meal_unique_restaurant_date_name")})
@Getter
@Setter
public class Meal extends NamedEntity {
    @Column(name = "price", nullable = false)
    @NotNull(message = "Price must not be null")
    @PositiveOrZero(message = "Price must be greater than or equal to 0")
    private Integer price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private Restaurant restaurant;

    @Column(name = "meal_date", nullable = false)
    @NotNull(message = "Date must not be null")
    private LocalDate date;

    @Override
    public String toString() {
        return "Meal{" +
                "id=" + this.getId() +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", restaurantId=" + restaurant.getId() +
                ", mealDate=" + date +
                '}';
    }
}