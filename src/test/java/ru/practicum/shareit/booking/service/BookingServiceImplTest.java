package ru.practicum.shareit.booking.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingGetDto;
import ru.practicum.shareit.booking.entity.Booking;
import ru.practicum.shareit.booking.entity.BookingState;
import ru.practicum.shareit.booking.exception.BookingNotFoundException;
import ru.practicum.shareit.booking.storage.BookingStorage;
import ru.practicum.shareit.item.entity.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.entity.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static ru.practicum.shareit.TestData.*;

@ExtendWith(MockitoExtension.class)
class BookingServiceImplTest {
    @Mock
    private BookingStorage storage;
    @Mock
    private UserService userService;
    @Mock
    private ItemService itemService;
    @InjectMocks
    BookingServiceImpl service;

    @Test
    void getBookingById() {
        LocalDateTime now = LocalDateTime.now();
        BookingGetDto required = prepareBookingGetDto(now);
        Booking booking = prepareBooking(now);
        when(storage.findById(anyLong())).thenReturn(Optional.of(booking));

        BookingGetDto bookingGetDto = service.getBookingById(1L, 1L);

        assertEquals(required, bookingGetDto);
    }

    @Test
    void addNewBooking() {
        LocalDateTime now = LocalDateTime.now();
        BookingDto bookingDto = prepareBookingDto(now);
        BookingGetDto expected = prepareBookingGetDto(now);
        Booking booking = prepareBooking(now);
        User user = prepareUser(2L);
        Item item = prepareItem();
        when(itemService.getFullItemDtoById(anyLong())).thenReturn(item);
        when(storage.save(any(Booking.class))).thenReturn(booking);
        when(userService.getUserById(anyLong())).thenReturn(user);

        BookingGetDto bookingGetDto = service.addNewBooking(bookingDto, 2L);

        assertEquals(expected, bookingGetDto);
        verify(storage, times(1)).save(any(Booking.class));
    }

    @Test
    void addNewBookingDateException() {
        LocalDateTime now = LocalDateTime.now();
        BookingDto bookingDto = prepareBookingDto(now);
        bookingDto.setStart(now);
        bookingDto.setEnd(now.minusDays(1));

        assertThrows(IllegalArgumentException.class, () -> service.addNewBooking(bookingDto, 2L));

    }

    @Test
    void getBookingsByBookerIdPageableAll() {
        LocalDateTime now = LocalDateTime.now();
        List<BookingGetDto> expected = List.of(prepareBookingGetDto(now));
        List<Booking> bookings = List.of(prepareBooking(now));
        doNothing().when(userService).checkUser(anyLong());
        when(storage.findByBookerIdOrderByStartDesc(anyLong(), any(Pageable.class))).thenReturn(bookings);

        List<BookingGetDto> bookingGetDtoList = service.getBookingsByBookerId(2L,
                BookingStateFilter.ALL,
                Optional.of(1),
                Optional.of(1));

        assertEquals(expected, bookingGetDtoList);
    }

    @Test
    void getBookingsByBookerIdAll() {
        LocalDateTime now = LocalDateTime.now();
        List<BookingGetDto> expected = List.of(prepareBookingGetDto(now));
        List<Booking> bookings = List.of(prepareBooking(now));
        doNothing().when(userService).checkUser(anyLong());
        when(storage.findByBookerIdOrderByStartDesc(anyLong())).thenReturn(bookings);

        List<BookingGetDto> bookingGetDtoList = service.getBookingsByBookerId(2L,
                BookingStateFilter.ALL,
                Optional.empty(),
                Optional.empty());

        assertEquals(expected, bookingGetDtoList);
    }

    @Test
    void getBookingsByBookerIdPageableWaiting() {
        LocalDateTime now = LocalDateTime.now();
        List<BookingGetDto> expected = List.of(prepareBookingGetDto(now));
        List<Booking> bookings = List.of(prepareBooking(now));
        doNothing().when(userService).checkUser(anyLong());
        when(storage.findByBookerIdAndStateOrderByStartDesc(anyLong(), any(BookingState.class), any(Pageable.class))).thenReturn(bookings);

        List<BookingGetDto> bookingGetDtoList = service.getBookingsByBookerId(2L,
                BookingStateFilter.WAITING,
                Optional.of(1),
                Optional.of(1));

        assertEquals(expected, bookingGetDtoList);
    }

    @Test
    void getBookingsByBookerIdWaiting() {
        LocalDateTime now = LocalDateTime.now();
        List<BookingGetDto> expected = List.of(prepareBookingGetDto(now));
        List<Booking> bookings = List.of(prepareBooking(now));
        doNothing().when(userService).checkUser(anyLong());
        when(storage.findByBookerIdAndStateOrderByStartDesc(anyLong(), any(BookingState.class))).thenReturn(bookings);

        List<BookingGetDto> bookingGetDtoList = service.getBookingsByBookerId(2L,
                BookingStateFilter.WAITING,
                Optional.empty(),
                Optional.empty());

        assertEquals(expected, bookingGetDtoList);
    }

    @Test
    void getBookingsByBookerIdPageableRejected() {
        LocalDateTime now = LocalDateTime.now();
        List<BookingGetDto> expected = List.of(prepareBookingGetDto(now));
        List<Booking> bookings = List.of(prepareBooking(now));
        doNothing().when(userService).checkUser(anyLong());
        when(storage.findByBookerIdAndStateOrderByStartDesc(anyLong(), any(BookingState.class), any(Pageable.class))).thenReturn(bookings);

        List<BookingGetDto> bookingGetDtoList = service.getBookingsByBookerId(2L,
                BookingStateFilter.REJECTED,
                Optional.of(1),
                Optional.of(1));

        assertEquals(expected, bookingGetDtoList);
    }

    @Test
    void getBookingsByBookerIdRejected() {
        LocalDateTime now = LocalDateTime.now();
        List<BookingGetDto> expected = List.of(prepareBookingGetDto(now));
        List<Booking> bookings = List.of(prepareBooking(now));
        doNothing().when(userService).checkUser(anyLong());
        when(storage.findByBookerIdAndStateOrderByStartDesc(anyLong(), any(BookingState.class))).thenReturn(bookings);

        List<BookingGetDto> bookingGetDtoList = service.getBookingsByBookerId(2L,
                BookingStateFilter.REJECTED,
                Optional.empty(),
                Optional.empty());

        assertEquals(expected, bookingGetDtoList);
    }

    @Test
    void getBookingsByBookerIdPageablePast() {
        LocalDateTime now = LocalDateTime.now();
        List<BookingGetDto> expected = List.of(prepareBookingGetDto(now));
        List<Booking> bookings = List.of(prepareBooking(now));
        doNothing().when(userService).checkUser(anyLong());
        when(storage.findByBookerIdAndEndBeforeOrderByStartDesc(anyLong(), any(LocalDateTime.class), any(Pageable.class))).thenReturn(bookings);

        List<BookingGetDto> bookingGetDtoList = service.getBookingsByBookerId(2L,
                BookingStateFilter.PAST,
                Optional.of(1),
                Optional.of(1));

        assertEquals(expected, bookingGetDtoList);
    }

    @Test
    void getBookingsByBookerIdPast() {
        LocalDateTime now = LocalDateTime.now();
        List<BookingGetDto> expected = List.of(prepareBookingGetDto(now));
        List<Booking> bookings = List.of(prepareBooking(now));
        doNothing().when(userService).checkUser(anyLong());
        when(storage.findByBookerIdAndEndBeforeOrderByStartDesc(anyLong(), any(LocalDateTime.class))).thenReturn(bookings);

        List<BookingGetDto> bookingGetDtoList = service.getBookingsByBookerId(2L,
                BookingStateFilter.PAST,
                Optional.empty(),
                Optional.empty());

        assertEquals(expected, bookingGetDtoList);
    }

    @Test
    void getBookingsByBookerIdPageableFuture() {
        LocalDateTime now = LocalDateTime.now();
        List<BookingGetDto> expected = List.of(prepareBookingGetDto(now));
        List<Booking> bookings = List.of(prepareBooking(now));
        doNothing().when(userService).checkUser(anyLong());
        when(storage.findByBookerIdAndStartAfterOrderByStartDesc(anyLong(), any(LocalDateTime.class), any(Pageable.class))).thenReturn(bookings);

        List<BookingGetDto> bookingGetDtoList = service.getBookingsByBookerId(2L,
                BookingStateFilter.FUTURE,
                Optional.of(1),
                Optional.of(1));

        assertEquals(expected, bookingGetDtoList);
    }

    @Test
    void getBookingsByBookerIdFuture() {
        LocalDateTime now = LocalDateTime.now();
        List<BookingGetDto> expected = List.of(prepareBookingGetDto(now));
        List<Booking> bookings = List.of(prepareBooking(now));
        doNothing().when(userService).checkUser(anyLong());
        when(storage.findByBookerIdAndStartAfterOrderByStartDesc(anyLong(), any(LocalDateTime.class))).thenReturn(bookings);

        List<BookingGetDto> bookingGetDtoList = service.getBookingsByBookerId(2L,
                BookingStateFilter.FUTURE,
                Optional.empty(),
                Optional.empty());

        assertEquals(expected, bookingGetDtoList);
    }

    @Test
    void getBookingsByBookerIdPageableCurrent() {
        LocalDateTime now = LocalDateTime.now();
        List<BookingGetDto> expected = List.of(prepareBookingGetDto(now));
        List<Booking> bookings = List.of(prepareBooking(now));
        doNothing().when(userService).checkUser(anyLong());
        when(storage.findByBookerIdAndStartBeforeAndEndAfterOrderByStartDesc(anyLong(),
                any(LocalDateTime.class),
                any(LocalDateTime.class),
                any(Pageable.class))).thenReturn(bookings);

        List<BookingGetDto> bookingGetDtoList = service.getBookingsByBookerId(2L,
                BookingStateFilter.CURRENT,
                Optional.of(1),
                Optional.of(1));

        assertEquals(expected, bookingGetDtoList);
    }

    @Test
    void getBookingsByBookerIdCurrent() {
        LocalDateTime now = LocalDateTime.now();
        List<BookingGetDto> expected = List.of(prepareBookingGetDto(now));
        List<Booking> bookings = List.of(prepareBooking(now));
        doNothing().when(userService).checkUser(anyLong());
        when(storage.findByBookerIdAndStartBeforeAndEndAfterOrderByStartDesc(anyLong(),
                any(LocalDateTime.class),
                any(LocalDateTime.class))).thenReturn(bookings);

        List<BookingGetDto> bookingGetDtoList = service.getBookingsByBookerId(2L,
                BookingStateFilter.CURRENT,
                Optional.empty(),
                Optional.empty());

        assertEquals(expected, bookingGetDtoList);
    }

    @Test
    void getBookingsByItemOwnerIdPageableAll() {
        LocalDateTime now = LocalDateTime.now();
        List<BookingGetDto> expected = List.of(prepareBookingGetDto(now));
        List<Booking> bookings = List.of(prepareBooking(now));
        doNothing().when(userService).checkUser(anyLong());
        when(storage.findByItemUserIdOrderByStartDesc(anyLong(), any(Pageable.class))).thenReturn(bookings);

        List<BookingGetDto> bookingGetDtoList = service.getBookingsByItemOwnerId(2L,
                BookingStateFilter.ALL,
                Optional.of(1),
                Optional.of(1));

        assertEquals(expected, bookingGetDtoList);
    }

    @Test
    void getBookingsByItemOwnerIdAll() {
        LocalDateTime now = LocalDateTime.now();
        List<BookingGetDto> expected = List.of(prepareBookingGetDto(now));
        List<Booking> bookings = List.of(prepareBooking(now));
        doNothing().when(userService).checkUser(anyLong());
        when(storage.findByItemUserIdOrderByStartDesc(anyLong())).thenReturn(bookings);

        List<BookingGetDto> bookingGetDtoList = service.getBookingsByItemOwnerId(2L,
                BookingStateFilter.ALL,
                Optional.empty(),
                Optional.empty());

        assertEquals(expected, bookingGetDtoList);
    }

    @Test
    void getBookingsByItemOwnerIdPageableWaiting() {
        LocalDateTime now = LocalDateTime.now();
        List<BookingGetDto> expected = List.of(prepareBookingGetDto(now));
        List<Booking> bookings = List.of(prepareBooking(now));
        doNothing().when(userService).checkUser(anyLong());
        when(storage.findByItemUserIdAndStateOrderByStartDesc(anyLong(),
                any(BookingState.class),
                any(Pageable.class))).thenReturn(bookings);

        List<BookingGetDto> bookingGetDtoList = service.getBookingsByItemOwnerId(2L,
                BookingStateFilter.WAITING,
                Optional.of(1),
                Optional.of(1));

        assertEquals(expected, bookingGetDtoList);
    }

    @Test
    void getBookingsByItemOwnerIdWaiting() {
        LocalDateTime now = LocalDateTime.now();
        List<BookingGetDto> expected = List.of(prepareBookingGetDto(now));
        List<Booking> bookings = List.of(prepareBooking(now));
        doNothing().when(userService).checkUser(anyLong());
        when(storage.findByItemUserIdAndStateOrderByStartDesc(anyLong(),
                any(BookingState.class))).thenReturn(bookings);

        List<BookingGetDto> bookingGetDtoList = service.getBookingsByItemOwnerId(2L,
                BookingStateFilter.WAITING,
                Optional.empty(),
                Optional.empty());

        assertEquals(expected, bookingGetDtoList);
    }

    @Test
    void getBookingsByItemOwnerIdPageableRejected() {
        LocalDateTime now = LocalDateTime.now();
        List<BookingGetDto> expected = List.of(prepareBookingGetDto(now));
        List<Booking> bookings = List.of(prepareBooking(now));
        doNothing().when(userService).checkUser(anyLong());
        when(storage.findByItemUserIdAndStateOrderByStartDesc(anyLong(),
                any(BookingState.class),
                any(Pageable.class))).thenReturn(bookings);

        List<BookingGetDto> bookingGetDtoList = service.getBookingsByItemOwnerId(2L,
                BookingStateFilter.REJECTED,
                Optional.of(1),
                Optional.of(1));

        assertEquals(expected, bookingGetDtoList);
    }

    @Test
    void getBookingsByItemOwnerIdRejected() {
        LocalDateTime now = LocalDateTime.now();
        List<BookingGetDto> expected = List.of(prepareBookingGetDto(now));
        List<Booking> bookings = List.of(prepareBooking(now));
        doNothing().when(userService).checkUser(anyLong());
        when(storage.findByItemUserIdAndStateOrderByStartDesc(anyLong(),
                any(BookingState.class))).thenReturn(bookings);

        List<BookingGetDto> bookingGetDtoList = service.getBookingsByItemOwnerId(2L,
                BookingStateFilter.REJECTED,
                Optional.empty(),
                Optional.empty());

        assertEquals(expected, bookingGetDtoList);
    }

    @Test
    void getBookingsByItemOwnerIdPageablePast() {
        LocalDateTime now = LocalDateTime.now();
        List<BookingGetDto> expected = List.of(prepareBookingGetDto(now));
        List<Booking> bookings = List.of(prepareBooking(now));
        doNothing().when(userService).checkUser(anyLong());
        when(storage.findByItemUserIdAndEndBeforeOrderByStartDesc(anyLong(),
                any(LocalDateTime.class),
                any(Pageable.class))).thenReturn(bookings);

        List<BookingGetDto> bookingGetDtoList = service.getBookingsByItemOwnerId(2L,
                BookingStateFilter.PAST,
                Optional.of(1),
                Optional.of(1));

        assertEquals(expected, bookingGetDtoList);
    }

    @Test
    void getBookingsByItemOwnerIdPast() {
        LocalDateTime now = LocalDateTime.now();
        List<BookingGetDto> expected = List.of(prepareBookingGetDto(now));
        List<Booking> bookings = List.of(prepareBooking(now));
        doNothing().when(userService).checkUser(anyLong());
        when(storage.findByItemUserIdAndEndBeforeOrderByStartDesc(anyLong(),
                any(LocalDateTime.class))).thenReturn(bookings);

        List<BookingGetDto> bookingGetDtoList = service.getBookingsByItemOwnerId(2L,
                BookingStateFilter.PAST,
                Optional.empty(),
                Optional.empty());

        assertEquals(expected, bookingGetDtoList);
    }

    @Test
    void getBookingsByItemOwnerIdPageableFuture() {
        LocalDateTime now = LocalDateTime.now();
        List<BookingGetDto> expected = List.of(prepareBookingGetDto(now));
        List<Booking> bookings = List.of(prepareBooking(now));
        doNothing().when(userService).checkUser(anyLong());
        when(storage.findByItemUserIdAndStartAfterOrderByStartDesc(anyLong(),
                any(LocalDateTime.class),
                any(Pageable.class))).thenReturn(bookings);

        List<BookingGetDto> bookingGetDtoList = service.getBookingsByItemOwnerId(2L,
                BookingStateFilter.FUTURE,
                Optional.of(1),
                Optional.of(1));

        assertEquals(expected, bookingGetDtoList);
    }

    @Test
    void getBookingsByItemOwnerIdFuture() {
        LocalDateTime now = LocalDateTime.now();
        List<BookingGetDto> expected = List.of(prepareBookingGetDto(now));
        List<Booking> bookings = List.of(prepareBooking(now));
        doNothing().when(userService).checkUser(anyLong());
        when(storage.findByItemUserIdAndStartAfterOrderByStartDesc(anyLong(),
                any(LocalDateTime.class))).thenReturn(bookings);

        List<BookingGetDto> bookingGetDtoList = service.getBookingsByItemOwnerId(2L,
                BookingStateFilter.FUTURE,
                Optional.empty(),
                Optional.empty());

        assertEquals(expected, bookingGetDtoList);
    }

    @Test
    void getBookingsByItemOwnerIdPageableCurrent() {
        LocalDateTime now = LocalDateTime.now();
        List<BookingGetDto> expected = List.of(prepareBookingGetDto(now));
        List<Booking> bookings = List.of(prepareBooking(now));
        doNothing().when(userService).checkUser(anyLong());
        when(storage.findByItemUserIdAndStartBeforeAndEndAfterOrderByStartDesc(anyLong(),
                any(LocalDateTime.class),
                any(LocalDateTime.class),
                any(Pageable.class))).thenReturn(bookings);

        List<BookingGetDto> bookingGetDtoList = service.getBookingsByItemOwnerId(2L,
                BookingStateFilter.CURRENT,
                Optional.of(1),
                Optional.of(1));

        assertEquals(expected, bookingGetDtoList);
    }

    @Test
    void getBookingsByItemOwnerIdCurrent() {
        LocalDateTime now = LocalDateTime.now();
        List<BookingGetDto> expected = List.of(prepareBookingGetDto(now));
        List<Booking> bookings = List.of(prepareBooking(now));
        doNothing().when(userService).checkUser(anyLong());
        when(storage.findByItemUserIdAndStartBeforeAndEndAfterOrderByStartDesc(anyLong(),
                any(LocalDateTime.class),
                any(LocalDateTime.class))).thenReturn(bookings);

        List<BookingGetDto> bookingGetDtoList = service.getBookingsByItemOwnerId(2L,
                BookingStateFilter.CURRENT,
                Optional.empty(),
                Optional.empty());

        assertEquals(expected, bookingGetDtoList);
    }

    @Test
    void updateBookingByItemOwner() {
        LocalDateTime now = LocalDateTime.now();
        BookingGetDto expected = prepareBookingGetDto(now);
        expected.setStatus(BookingState.APPROVED);
        Booking booking = prepareBooking(now);
        Booking savedBooking = prepareBooking(now);
        savedBooking.setState(BookingState.APPROVED);
        when(storage.findById(anyLong())).thenReturn(Optional.of(booking));
        when(storage.save(any(Booking.class))).thenReturn(savedBooking);

        BookingGetDto bookingGetDto = service.updateBookingByItemOwner(1L, 1L, true);

        assertEquals(expected, bookingGetDto);
        verify(storage, times(1)).save(any(Booking.class));
    }

    @Test
    void updateBookingByItemOwnerStateException() {
        LocalDateTime now = LocalDateTime.now();
        Booking booking = prepareBooking(now);
        booking.setState(BookingState.APPROVED);
        when(storage.findById(anyLong())).thenReturn(Optional.of(booking));

        assertThrows(IllegalArgumentException.class, () -> service.updateBookingByItemOwner(1L,
                1L,
                true));
    }

    @Test
    void updateBookingByItemOwnerRejected() {
        LocalDateTime now = LocalDateTime.now();
        BookingGetDto expected = prepareBookingGetDto(now);
        expected.setStatus(BookingState.REJECTED);
        Booking booking = prepareBooking(now);
        Booking savedBooking = prepareBooking(now);
        savedBooking.setState(BookingState.REJECTED);
        when(storage.findById(anyLong())).thenReturn(Optional.of(booking));
        when(storage.save(any(Booking.class))).thenReturn(savedBooking);

        BookingGetDto bookingGetDto = service.updateBookingByItemOwner(1L, 1L, false);

        assertEquals(expected, bookingGetDto);
        verify(storage, times(1)).save(any(Booking.class));
    }

    @Test
    void updateBookingByItemOwnerNotFoundException() {
        LocalDateTime now = LocalDateTime.now();
        Booking booking = prepareBooking(now);
        when(storage.findById(anyLong())).thenReturn(Optional.of(booking));

        assertThrows(BookingNotFoundException.class, () -> service.updateBookingByItemOwner(1L,
                2L,
                false));
    }
}