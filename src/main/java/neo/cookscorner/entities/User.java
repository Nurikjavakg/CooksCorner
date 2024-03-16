package neo.cookscorner.entities;

import jakarta.persistence.*;
import lombok.*;
import neo.cookscorner.enums.Role;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "users")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User implements UserDetails {
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "users_gen")
    @SequenceGenerator(name = "users_gen",
            sequenceName = "users_seq",
            allocationSize = 1)
    private Long userId;
    private String name;
    private String biography;
    private String email;
    private String password;
    @Enumerated(EnumType.STRING)
    private Role role;
    @ElementCollection
    private List<Long> following;
    @ElementCollection
    private List<Long> followers;
    @OneToMany(mappedBy = "owner", cascade = {CascadeType.DETACH,
            CascadeType.MERGE,
            CascadeType.REMOVE,
            CascadeType.REFRESH})
    private List<Recipe> recipes;
    @OneToMany
    @JoinTable(
            name = "users_images",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "image_id")
    )
    private List<Image> images;
    @OneToMany(mappedBy = "user",
            cascade = {CascadeType.DETACH,
                       CascadeType.MERGE,
                       CascadeType.REFRESH,
                       CascadeType.REMOVE})
    private List<Favorite> favorites;
    @OneToMany(mappedBy = "user",
            cascade = {CascadeType.DETACH,
                    CascadeType.MERGE,
                    CascadeType.REFRESH,
                    CascadeType.REMOVE})
    private List<Like> likes;

    public void addFollowers(User user) {
        followers.add(user.getUserId());
    }

    public void addFollowing(User user) {
        following.add(user.getUserId());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}