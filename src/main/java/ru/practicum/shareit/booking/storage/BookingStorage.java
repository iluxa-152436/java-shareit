package ru.practicum.shareit.booking.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.booking.entity.Booking;
import ru.practicum.shareit.booking.entity.BookingState;
import ru.practicum.shareit.item.entity.Item;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingStorage extends JpaRepository<Booking, Long> {

    List<Booking> findByBookerIdOrderByStartDesc(long bookerId);

    List<Booking> findByBookerIdAndStateOrderByStartDesc(long bookerId, BookingState state);

    List<Booking> findByBookerIdAndStartAfterOrderByStartDesc(long bookerId,
                                                              LocalDateTime dateTime);

    List<Booking> findByBookerIdAndEndBeforeOrderByStartDesc(long bookerId,
                                                             LocalDateTime dateTime);

    List<Booking> findByBookerIdAndStartBeforeAndEndAfterOrderByStartDesc(long bookerId,
                                                                          LocalDateTime dateTime1,
                                                                          LocalDateTime dateTime2);

    List<Booking> findByItemUserIdOrderByStartDesc(long ownerId);

    List<Booking> findByItemUserIdAndStateOrderByStartDesc(long ownerId, BookingState state);

    List<Booking> findByItemUserIdAndStartAfterOrderByStartDesc(long ownerId, LocalDateTime dateTime);

    List<Booking> findByItemUserIdAndEndBeforeOrderByStartDesc(long ownerId, LocalDateTime dateTime);

    List<Booking> findByItemUserIdAndStartBeforeAndEndAfterOrderByStartDesc(long ownerId,
                                                                            LocalDateTime dateTime1,
                                                                            LocalDateTime dateTime2);

    List<Booking> findByItemIn(List<Item> items);

    @Query("select count (bo) " +
            "from Booking as bo " +
            "join bo.booker as b " +
            "join bo.item as it " +
            "where b.id = ?1 and it.id = ?2 and bo.state = ?3 and bo.end < ?4")
    int findCountByBookerIdAndItemIdAndState(long userId,
                                             long itemId,
                                             BookingState bookingState,
                                             LocalDateTime dateTime);
}
