package name.cnsler.restvote.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"user_id", "vote_date"}, name = "vote_unique_user_date_idx")})
@Getter
@Setter
public class Vote extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @NotNull(message = "User must not be null")
    @JsonIgnore
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", nullable = false)
    @NotNull(message = "Restaurant must not be null")
    private Restaurant restaurant;

    @Column(name = "vote_date", nullable = false)
    @NotNull(message = "Date must not be null")
    private LocalDate voteDate;

    @Override
    public String toString() {
        return "Vote{" +
                "id=" + id +
                ", userId=" + user.getId() +
                ", restaurantId=" + restaurant.getId() +
                ", voteDate=" + voteDate +
                '}';
    }
}