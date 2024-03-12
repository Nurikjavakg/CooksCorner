package neo.cookscorner.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "favorites")
@AllArgsConstructor
@NoArgsConstructor
public class Favorite {
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "favorites_gen")
    @SequenceGenerator(name = "favorites_gen",
            sequenceName = "favorites_seq",
            allocationSize = 1)
    private Long id;
    @ManyToOne (cascade = {CascadeType.DETACH,
                            CascadeType.MERGE,
                            CascadeType.REFRESH})
    private User user;
    // @ManyToOne
    // private Product product;
}
