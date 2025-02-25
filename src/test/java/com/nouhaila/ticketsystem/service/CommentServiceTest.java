package com.nouhaila.ticketsystem.service;

import com.nouhaila.ticketsystem.dto.CommentDTO;
import com.nouhaila.ticketsystem.exception.RunTimeException;
import com.nouhaila.ticketsystem.model.Comment;
import com.nouhaila.ticketsystem.model.Ticket;
import com.nouhaila.ticketsystem.model.User;
import com.nouhaila.ticketsystem.repository.CommentRepository;
import com.nouhaila.ticketsystem.repository.TicketRepository;
import com.nouhaila.ticketsystem.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CommentServiceTest {

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private TicketRepository ticketRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CommentService commentService;

    private Ticket ticket;
    private User user;
    private Comment comment;
    private CommentDTO commentDTO;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setUsername("test_user");

        ticket = Ticket.builder()
                .id(1L)
                .title("Test Ticket")
                .build();

        comment = Comment.builder()
                .id(1L)
                .content("This is a test comment")
                .ticket(ticket)
                .user(user)
                .build();

        commentDTO = CommentDTO.builder()
                .id(1L)
                .content("This is a test comment")
                .userId(user.getId())
                .build();
    }

    @Test
    void testGetCommentsByTicketId() {
        when(commentRepository.findByTicketId(1L)).thenReturn(Arrays.asList(comment));

        List<CommentDTO> result = commentService.getCommentsByTicketId(1L);

        assertEquals(1, result.size());
        assertEquals("This is a test comment", result.get(0).getContent());
        verify(commentRepository, times(1)).findByTicketId(1L);
    }

    @Test
    void testAddComment_Success() {
        when(ticketRepository.findById(1L)).thenReturn(Optional.of(ticket));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(commentRepository.save(any(Comment.class))).thenReturn(comment);

        CommentDTO result = commentService.addComment(1L, 1L, "This is a test comment");

        assertNotNull(result);
        assertEquals("This is a test comment", result.getContent());
        verify(commentRepository, times(1)).save(any(Comment.class));
    }

    @Test
    void testAddComment_TicketNotFound() {
        when(ticketRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RunTimeException.class, () -> commentService.addComment(1L, 1L, "Test comment"));
    }

    @Test
    void testAddComment_UserNotFound() {
        when(ticketRepository.findById(1L)).thenReturn(Optional.of(ticket));
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RunTimeException.class, () -> commentService.addComment(1L, 1L, "Test comment"));
    }
}
