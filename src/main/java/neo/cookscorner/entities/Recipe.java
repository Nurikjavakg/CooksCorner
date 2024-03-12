package neo.cookscorner.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.Map;

@Entity
@Table(name = "recipes")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Recipe {
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "recipes_gen")
    @SequenceGenerator(name = "recipes_gen",
            sequenceName = "recipes_seq",
            allocationSize = 1)
    private Long recipeId;
    private String recipeName;
    private String description;
    @OneToMany( mappedBy = "recipe", cascade = CascadeType.ALL)
    private List<Ingredient> ingredients;
    @OneToMany
    @JoinTable(
            name = "recipes_images",
            joinColumns = @JoinColumn(name = "recipe_id"),
            inverseJoinColumns = @JoinColumn(name = "image_id")
    )
    private List<Image> images;
}
