package com.aisupport.api.service;

import com.aisupport.api.exception.ResourceNotFoundException;
import com.aisupport.api.model.Business;
import com.aisupport.api.model.ChatSession;
import com.aisupport.api.model.Customer;
import com.aisupport.api.repository.ChatSessionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ChatServiceTest {

    @Mock
    private ChatSessionRepository chatSessionRepository;

    @InjectMocks
    private ChatService chatService;

    @Test
    void getSessionHistory_rejectsInvalidCustomerSessionId() {
        Customer customer = Customer.builder().sessionId("valid-session-id").build();
        ChatSession session = ChatSession.builder()
                .id(1L)
                .customer(customer)
                .business(Business.builder().id(1L).name("Test").build())
                .build();

        when(chatSessionRepository.findById(1L)).thenReturn(Optional.of(session));

        assertThrows(ResourceNotFoundException.class,
                () -> chatService.getSessionHistory(1L, "wrong-session-id"));
    }

    @Test
    void getSessionHistory_rejectsMissingCustomerSessionId() {
        assertThrows(com.aisupport.api.exception.ForbiddenException.class,
                () -> chatService.getSessionHistory(1L, ""));
    }
}
