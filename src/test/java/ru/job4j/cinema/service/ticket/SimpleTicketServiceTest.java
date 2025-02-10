package ru.job4j.cinema.service.ticket;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.job4j.cinema.dto.TicketDto;
import ru.job4j.cinema.model.Ticket;
import ru.job4j.cinema.repository.ticket.TicketRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SimpleTicketServiceTest {

    @Mock
    private TicketRepository ticketRepository;

    @InjectMocks
    private SimpleTicketService simpleTicketService;

    private final Ticket testTicket = new Ticket(1, 1, 5, 10, 1001);

    @BeforeEach
    void setUp() {
        reset(ticketRepository);
    }

    @Test
    void whenGetByIdThenReturnTicketDto() {

        when(ticketRepository.getById(1)).thenReturn(Optional.of(testTicket));

        Optional<TicketDto> result = simpleTicketService.getById(1);

        assertTrue(result.isPresent());
        assertEquals(1, result.get().getId());
        assertEquals(1, result.get().getSessionId());
        assertEquals(5, result.get().getRowNumber());
        assertEquals(10, result.get().getPlaceNumber());
        assertEquals(1001, result.get().getUserId());
    }

    @Test
    void whenGetByIdNotFoundThenReturnEmpty() {

        when(ticketRepository.getById(99)).thenReturn(Optional.empty());

        Optional<TicketDto> result = simpleTicketService.getById(99);

        assertTrue(result.isEmpty());
    }
}