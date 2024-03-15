package neo.cookscorner.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "likes")
@AllArgsConstructor
@NoArgsConstructor
public class Like {
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "likes_gen")
    @SequenceGenerator(name = "likes_gen",
            sequenceName = "likes_seq",
            allocationSize = 1)
    private Long id;
    @ManyToOne(cascade = {CascadeType.DETACH,
                          CascadeType.MERGE,
                          CascadeType.REFRESH})
    private User user;
    @ManyToOne(cascade = {CascadeType.DETACH,
            CascadeType.MERGE,
            CascadeType.REFRESH})
    private Recipe recipe;
   // @ManyToOne
   // private Product product;
}