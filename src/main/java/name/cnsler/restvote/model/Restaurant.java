package name.cnsler.restvote.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;

@Entity
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"name"})})
@Getter
public class Restaurant extends NamedEntity {
    @Override
    public String toString() {
        return "Restaurant{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}