package ru.practicum.shareit.user.entity;

import lombok.*;

import javax.persistence.*;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "app_users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Exclude
    private long id;
    @Column(name = "name", nullable = false, length = 40)
    private String name;
    @Column(name = "email", nullable = false, unique = true, length = 40)
    private String email;

    public User(User user) {
        this(user.getId(), user.getName(), user.getEmail());
    }
}
