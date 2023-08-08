package ru.practicum.shareit.booking.entity;

import lombok.*;
import ru.practicum.shareit.item.entity.Item;
import ru.practicum.shareit.user.entity.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "bookings")
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @ManyToOne
    @JoinColumn(name = "item_id")
    @ToString.Exclude
    private Item item;
    @ManyToOne
    @JoinColumn(name = "user_id")
    @ToString.Exclude
    private User booker;
    @Enumerated(EnumType.STRING)
    @Column(name = "state", nullable = false, length = 10)
    private BookingState state;
    @Column(name = "start_date", nullable = false)
    private LocalDateTime start;
    @Column(name = "end_date", nullable = false)
    private LocalDateTime end;
}
