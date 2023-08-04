package ru.practicum.shareit.booking.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
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
    private BookingService service;


    @BeforeEach
    void prepare() {
        service = new BookingServiceImpl(storage, userService, itemService);
    }

    @Test
    void getBookingByIdTest() {
        LocalDateTime now = LocalDateTime.now();
        BookingGetDto required = prepareBookingGetDto(now);
        Booking booking = prepareBooking(now);
        when(storage.findById(anyLong())).thenReturn(Optional.of(booking));

        BookingGetDto bookingGetDto = service.getBookingById(1L, 1L);

        assertEquals(required, bookingGetDto);
    }

    @Test
    void addNewBookingTest() {
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
    void addNewBookingDateExceptionTest() {
        LocalDateTime now = LocalDateTime.now();
        BookingDto bookingDto = prepareBookingDto(now);
        bookingDto.setStart(now);
        bookingDto.setEnd(now.minusDays(1));

        assertThrows(IllegalArgumentException.class, () -> service.addNewBooking(bookingDto, 2L));
    }

    @Test
    void getBookingsByBookerIdPageableAllTest() {
        LocalDateTime now = LocalDateTime.now();
        List<BookingGetDto> expected = List.of(prepareBookingGetDto(now));
        List<Booking> bookings = List.of(prepareBooking(now));
        doNothing().when(userService).checkUser(anyLong());
        when(storage.findByBookerIdOrderByStartDesc(anyLong(), any(Pageable.class)))
                .thenReturn(new PageImpl<>(bookings));

        List<BookingGetDto> bookingGetDtoList = service.getBookingsByBookerId(2L,
                BookingStateFilter.ALL,
                Optional.of(1),
                Optional.of(1));

        assertEquals(expected, bookingGetDtoList);
    }

    @Test
    void getBookingsByBookerIdAllTest() {
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
    void getBookingsByBookerIdPageableWaitingTest() {
        LocalDateTime now = LocalDateTime.now();
        List<BookingGetDto> expected = List.of(prepareBookingGetDto(now));
        List<Booking> bookings = List.of(prepareBooking(now));
        doNothing().when(userService).checkUser(anyLong());
        when(storage.findByBookerIdAndStateOrderByStartDesc(anyLong(), any(BookingState.class), any(Pageable.class)))
                .thenReturn(new PageImpl<>(bookings));

        List<BookingGetDto> bookingGetDtoList = service.getBookingsByBookerId(2L,
                BookingStateFilter.WAITING,
                Optional.of(1),
                Optional.of(1));

        assertEquals(expected, bookingGetDtoList);
    }

    @Test
    void getBookingsByBookerIdWaitingTest() {
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
    void getBookingsByBookerIdPageableRejectedTest() {
        LocalDateTime now = LocalDateTime.now();
        List<BookingGetDto> expected = List.of(prepareBookingGetDto(now));
        List<Booking> bookings = List.of(prepareBooking(now));
        doNothing().when(userService).checkUser(anyLong());
        when(storage
                .findByBookerIdAndStateOrderByStartDesc(anyLong(), any(BookingState.class), any(Pageable.class)))
                .thenReturn(new PageImpl<>(bookings));

        List<BookingGetDto> bookingGetDtoList = service.getBookingsByBookerId(2L,
                BookingStateFilter.REJECTED,
                Optional.of(1),
                Optional.of(1));

        assertEquals(expected, bookingGetDtoList);
    }

    @Test
    void getBookingsByBookerIdRejectedTest() {
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
    void getBookingsByBookerIdPageablePastTest() {
        LocalDateTime now = LocalDateTime.now();
        List<BookingGetDto> expected = List.of(prepareBookingGetDto(now));
        List<Booking> bookings = List.of(prepareBooking(now));
        doNothing().when(userService).checkUser(anyLong());
        when(storage
                .findByBookerIdAndEndBeforeOrderByStartDesc(anyLong(), any(LocalDateTime.class), any(Pageable.class)))
                .thenReturn(new PageImpl<>(bookings));

        List<BookingGetDto> bookingGetDtoList = service.getBookingsByBookerId(2L,
                BookingStateFilter.PAST,
                Optional.of(1),
                Optional.of(1));

        assertEquals(expected, bookingGetDtoList);
    }

    @Test
    void getBookingsByBookerIdPastTest() {
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
    void getBookingsByBookerIdPageableFutureTest() {
        LocalDateTime now = LocalDateTime.now();
        List<BookingGetDto> expected = List.of(prepareBookingGetDto(now));
        List<Booking> bookings = List.of(prepareBooking(now));
        doNothing().when(userService).checkUser(anyLong());
        when(storage
                .findByBookerIdAndStartAfterOrderByStartDesc(anyLong(), any(LocalDateTime.class), any(Pageable.class)))
                .thenReturn(new PageImpl<>(bookings));

        List<BookingGetDto> bookingGetDtoList = service.getBookingsByBookerId(2L,
                BookingStateFilter.FUTURE,
                Optional.of(1),
                Optional.of(1));

        assertEquals(expected, bookingGetDtoList);
    }

    @Test
    void getBookingsByBookerIdFutureTest() {
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
    void getBookingsByBookerIdPageableCurrentTest() {
        LocalDateTime now = LocalDateTime.now();
        List<BookingGetDto> expected = List.of(prepareBookingGetDto(now));
        List<Booking> bookings = List.of(prepareBooking(now));
        doNothing().when(userService).checkUser(anyLong());
        when(storage.findByBookerIdAndStartBeforeAndEndAfterOrderByStartDesc(anyLong(),
                any(LocalDateTime.class),
                any(LocalDateTime.class),
                any(Pageable.class))).thenReturn(new PageImpl<>(bookings));

        List<BookingGetDto> bookingGetDtoList = service.getBookingsByBookerId(2L,
                BookingStateFilter.CURRENT,
                Optional.of(1),
                Optional.of(1));

        assertEquals(expected, bookingGetDtoList);
    }

    @Test
    void getBookingsByBookerIdCurrentTest() {
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
    void getBookingsByItemOwnerIdPageableAllTest() {
        LocalDateTime now = LocalDateTime.now();
        List<BookingGetDto> expected = List.of(prepareBookingGetDto(now));
        List<Booking> bookings = List.of(prepareBooking(now));
        doNothing().when(userService).checkUser(anyLong());
        when(storage.findByItemUserIdOrderByStartDesc(anyLong(), any(Pageable.class)))
                .thenReturn(new PageImpl<>(bookings));

        List<BookingGetDto> bookingGetDtoList = service.getBookingsByItemOwnerId(2L,
                BookingStateFilter.ALL,
                Optional.of(1),
                Optional.of(1));

        assertEquals(expected, bookingGetDtoList);
    }

    @Test
    void getBookingsByItemOwnerIdAllTest() {
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
    void getBookingsByItemOwnerIdPageableWaitingTest() {
        LocalDateTime now = LocalDateTime.now();
        List<BookingGetDto> expected = List.of(prepareBookingGetDto(now));
        List<Booking> bookings = List.of(prepareBooking(now));
        doNothing().when(userService).checkUser(anyLong());
        when(storage.findByItemUserIdAndStateOrderByStartDesc(anyLong(),
                any(BookingState.class),
                any(Pageable.class))).thenReturn(new PageImpl<>(bookings));

        List<BookingGetDto> bookingGetDtoList = service.getBookingsByItemOwnerId(2L,
                BookingStateFilter.WAITING,
                Optional.of(1),
                Optional.of(1));

        assertEquals(expected, bookingGetDtoList);
    }

    @Test
    void getBookingsByItemOwnerIdWaitingTest() {
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
    void getBookingsByItemOwnerIdPageableRejectedTest() {
        LocalDateTime now = LocalDateTime.now();
        List<BookingGetDto> expected = List.of(prepareBookingGetDto(now));
        List<Booking> bookings = List.of(prepareBooking(now));
        doNothing().when(userService).checkUser(anyLong());
        when(storage.findByItemUserIdAndStateOrderByStartDesc(anyLong(),
                any(BookingState.class),
                any(Pageable.class))).thenReturn(new PageImpl<>(bookings));

        List<BookingGetDto> bookingGetDtoList = service.getBookingsByItemOwnerId(2L,
                BookingStateFilter.REJECTED,
                Optional.of(1),
                Optional.of(1));

        assertEquals(expected, bookingGetDtoList);
    }

    @Test
    void getBookingsByItemOwnerIdRejectedTest() {
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
    void getBookingsByItemOwnerIdPageablePastTest() {
        LocalDateTime now = LocalDateTime.now();
        List<BookingGetDto> expected = List.of(prepareBookingGetDto(now));
        List<Booking> bookings = List.of(prepareBooking(now));
        doNothing().when(userService).checkUser(anyLong());
        when(storage.findByItemUserIdAndEndBeforeOrderByStartDesc(anyLong(),
                any(LocalDateTime.class),
                any(Pageable.class))).thenReturn(new PageImpl<>(bookings));

        List<BookingGetDto> bookingGetDtoList = service.getBookingsByItemOwnerId(2L,
                BookingStateFilter.PAST,
                Optional.of(1),
                Optional.of(1));

        assertEquals(expected, bookingGetDtoList);
    }

    @Test
    void getBookingsByItemOwnerIdPastTest() {
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
    void getBookingsByItemOwnerIdPageableFutureTest() {
        LocalDateTime now = LocalDateTime.now();
        List<BookingGetDto> expected = List.of(prepareBookingGetDto(now));
        List<Booking> bookings = List.of(prepareBooking(now));
        doNothing().when(userService).checkUser(anyLong());
        when(storage.findByItemUserIdAndStartAfterOrderByStartDesc(anyLong(),
                any(LocalDateTime.class),
                any(Pageable.class))).thenReturn(new PageImpl<>(bookings));

        List<BookingGetDto> bookingGetDtoList = service.getBookingsByItemOwnerId(2L,
                BookingStateFilter.FUTURE,
                Optional.of(1),
                Optional.of(1));

        assertEquals(expected, bookingGetDtoList);
    }

    @Test
    void getBookingsByItemOwnerIdFutureTest() {
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
    void getBookingsByItemOwnerIdPageableCurrentTest() {
        LocalDateTime now = LocalDateTime.now();
        List<BookingGetDto> expected = List.of(prepareBookingGetDto(now));
        List<Booking> bookings = List.of(prepareBooking(now));
        doNothing().when(userService).checkUser(anyLong());
        when(storage.findByItemUserIdAndStartBeforeAndEndAfterOrderByStartDesc(anyLong(),
                any(LocalDateTime.class),
                any(LocalDateTime.class),
                any(Pageable.class))).thenReturn(new PageImpl<>(bookings));

        List<BookingGetDto> bookingGetDtoList = service.getBookingsByItemOwnerId(2L,
                BookingStateFilter.CURRENT,
                Optional.of(1),
                Optional.of(1));

        assertEquals(expected, bookingGetDtoList);
    }

    @Test
    void getBookingsByItemOwnerIdCurrentTest() {
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
    void updateBookingByItemOwnerTest() {
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
    void updateBookingByItemOwnerStateExceptionTest() {
        LocalDateTime now = LocalDateTime.now();
        Booking booking = prepareBooking(now);
        booking.setState(BookingState.APPROVED);
        when(storage.findById(anyLong())).thenReturn(Optional.of(booking));

        assertThrows(IllegalArgumentException.class, () -> service.updateBookingByItemOwner(1L,
                1L,
                true));
    }

    @Test
    void updateBookingByItemOwnerRejectedTest() {
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
    void updateBookingByItemOwnerNotFoundExceptionTest() {
        LocalDateTime now = LocalDateTime.now();
        Booking booking = prepareBooking(now);
        when(storage.findById(anyLong())).thenReturn(Optional.of(booking));

        assertThrows(BookingNotFoundException.class, () -> service.updateBookingByItemOwner(1L,
                2L,
                false));
    }
}