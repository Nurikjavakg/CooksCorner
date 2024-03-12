package neo.cookscorner.entities;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "ingredients")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Ingredient {
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "ingredients_gen")
    @SequenceGenerator(name = "ingredients_gen",
            sequenceName = "ingredients_seq",
            allocationSize = 1)
    private Long ingredientId;
    private String name;
    private String quantity;

    @ManyToOne
    @JoinColumn(name = "recipe_id")
    private Recipe recipe;
}
