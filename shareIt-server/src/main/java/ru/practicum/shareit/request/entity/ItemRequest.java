package ru.practicum.shareit.request.entity;

import lombok.*;
import org.apache.commons.lang3.builder.ToStringExclude;
import ru.practicum.shareit.user.entity.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "requests")
@Data
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class ItemRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Exclude
    private long id;
    @Column(name = "description", length = 200)
    private String description;
    @ManyToOne
    @JoinColumn(name = "user_id")
    @ToStringExclude
    private User requester;
    @Column(name = "created")
    private LocalDateTime created;
}
