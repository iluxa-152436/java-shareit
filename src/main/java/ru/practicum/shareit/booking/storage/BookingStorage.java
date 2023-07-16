package ru.practicum.shareit.booking.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.booking.entity.Booking;
import ru.practicum.shareit.booking.entity.BookingState;
import ru.practicum.shareit.item.model.Item;

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

    @Query("select bo " +
            "from Booking as bo " +
            "join bo.item as it " +
            "join it.user as u " +
            "where u.id = ?1 " +
            "order by bo.start desc")
    List<Booking> findByOwnerIdOrderByStartDesc(long ownerId);

    @Query("select bo " +
            "from Booking as bo " +
            "join bo.item as it " +
            "join it.user as u " +
            "where u.id = ?1 and bo.state = ?2 " +
            "order by bo.start desc")
    List<Booking> findByOwnerIdAndStateOrderByStartDesc(long ownerId, BookingState state);

    @Query("select bo " +
            "from Booking as bo " +
            "join bo.item as it " +
            "join it.user as u " +
            "where u.id = ?1 and bo.start > ?2 " +
            "order by bo.start desc")
    List<Booking> findByOwnerIdAndStateAndStartAfterOrderByStartDesc(long ownerId, LocalDateTime dateTime);

    @Query("select bo " +
            "from Booking as bo " +
            "join bo.item as it " +
            "join it.user as u " +
            "where u.id = ?1 and bo.end < ?2 " +
            "order by bo.start desc")
    List<Booking> findByOwnerIdAndStateAndEndBeforeOrderByStartDesc(long ownerId, LocalDateTime dateTime);

    @Query("select bo " +
            "from Booking as bo " +
            "join bo.item as it " +
            "join it.user as u " +
            "where u.id = ?1 and bo.start < ?2 and bo.end > ?2 " +
            "order by bo.start desc")
    List<Booking> findByOwnerIdAndStateAndStartBeforeAndEndAfterOrderByStartDesc(long ownerId, LocalDateTime dateTime);

    List<Booking> findByItemIn(List<Item> items);
}
