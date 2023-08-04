package ru.practicum.shareit.booking.storage;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.booking.entity.Booking;
import ru.practicum.shareit.booking.entity.BookingState;
import ru.practicum.shareit.item.entity.Item;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingStorage extends JpaRepository<Booking, Long> {

    List<Booking> findByBookerIdOrderByStartDesc(long bookerId);

    Page<Booking> findByBookerIdOrderByStartDesc(long bookerId, Pageable pageable);

    List<Booking> findByBookerIdAndStateOrderByStartDesc(long bookerId, BookingState state);

    Page<Booking> findByBookerIdAndStateOrderByStartDesc(long bookerId, BookingState state, Pageable pageable);

    List<Booking> findByBookerIdAndStartAfterOrderByStartDesc(long bookerId,
                                                              LocalDateTime dateTime);

    Page<Booking> findByBookerIdAndStartAfterOrderByStartDesc(long bookerId,
                                                              LocalDateTime dateTime,
                                                              Pageable pageable);

    List<Booking> findByBookerIdAndEndBeforeOrderByStartDesc(long bookerId,
                                                             LocalDateTime dateTime);

    Page<Booking> findByBookerIdAndEndBeforeOrderByStartDesc(long bookerId,
                                                             LocalDateTime dateTime,
                                                             Pageable pageable);

    List<Booking> findByBookerIdAndStartBeforeAndEndAfterOrderByStartDesc(long bookerId,
                                                                          LocalDateTime dateTime1,
                                                                          LocalDateTime dateTime2);

    Page<Booking> findByBookerIdAndStartBeforeAndEndAfterOrderByStartDesc(long bookerId,
                                                                          LocalDateTime dateTime1,
                                                                          LocalDateTime dateTime2,
                                                                          Pageable pageable);

    List<Booking> findByItemUserIdOrderByStartDesc(long ownerId);

    List<Booking> findByItemUserIdAndStateOrderByStartDesc(long ownerId, BookingState state);

    List<Booking> findByItemUserIdAndStartAfterOrderByStartDesc(long ownerId, LocalDateTime dateTime);

    List<Booking> findByItemUserIdAndEndBeforeOrderByStartDesc(long ownerId, LocalDateTime dateTime);

    List<Booking> findByItemUserIdAndStartBeforeAndEndAfterOrderByStartDesc(long ownerId,
                                                                            LocalDateTime dateTime1,
                                                                            LocalDateTime dateTime2);

    Page<Booking> findByItemUserIdOrderByStartDesc(long ownerId, Pageable pageable);

    Page<Booking> findByItemUserIdAndStateOrderByStartDesc(long ownerId, BookingState state, Pageable pageable);

    Page<Booking> findByItemUserIdAndStartAfterOrderByStartDesc(long ownerId,
                                                                LocalDateTime dateTime,
                                                                Pageable pageable);

    Page<Booking> findByItemUserIdAndEndBeforeOrderByStartDesc(long ownerId, LocalDateTime dateTime, Pageable pageable);

    Page<Booking> findByItemUserIdAndStartBeforeAndEndAfterOrderByStartDesc(long ownerId,
                                                                            LocalDateTime dateTime1,
                                                                            LocalDateTime dateTime2,
                                                                            Pageable pageable);

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
