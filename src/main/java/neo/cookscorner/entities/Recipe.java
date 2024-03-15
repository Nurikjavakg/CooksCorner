package neo.cookscorner.entities;

import jakarta.persistence.*;
import lombok.*;
import neo.cookscorner.enums.CategoryOfMeal;
import neo.cookscorner.enums.Difficulty;
import java.util.List;

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
    @Enumerated(EnumType.STRING)
    private Difficulty difficulty;
    @Enumerated(EnumType.STRING)
    private CategoryOfMeal categoryOfMeal;
    private String preparationTime;
    @ManyToOne(cascade = {
            CascadeType.REFRESH,
            CascadeType.DETACH,
            CascadeType.MERGE},fetch=FetchType.LAZY)
    private User choose;

    @OneToMany( mappedBy = "recipe", cascade = CascadeType.ALL)
    private List<Ingredient> ingredients;
    @ManyToOne(cascade = {
            CascadeType.REFRESH,
            CascadeType.DETACH,
            CascadeType.MERGE},fetch=FetchType.LAZY)
    private User owner;
    @OneToMany(mappedBy = "recipe", cascade = {CascadeType.DETACH,
            CascadeType.MERGE,
            CascadeType.REMOVE,
            CascadeType.REFRESH})
    private List<Like> likes;
    @OneToMany(mappedBy = "recipe", cascade = {CascadeType.DETACH,
            CascadeType.MERGE,
            CascadeType.REMOVE,
            CascadeType.REFRESH})
    private List<Favorite> favorites;

    @OneToMany
    @JoinTable(
            name = "recipes_images",
            joinColumns = @JoinColumn(name = "recipe_id"),
            inverseJoinColumns = @JoinColumn(name = "image_id")
    )
    private List<Image> images;
}
