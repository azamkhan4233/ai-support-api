package com.aisupport.api.service;

import com.aisupport.api.dto.*;
import com.aisupport.api.exception.ResourceNotFoundException;
import com.aisupport.api.model.*;
import com.aisupport.api.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class ChatService {

    @Autowired
    private ChatSessionRepository chatSessionRepository;

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private BusinessRepository businessRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private AIService aiService;

    @Autowired
    private LeadService leadService;

    public ChatSession startSession(SessionStartDTO request) {
        Business business = businessRepository.findById(request.getBusinessId())
                .orElseThrow(() -> new ResourceNotFoundException("Business not found"));

        // Find or create customer
        Customer customer;
        if (request.getCustomerSessionId() != null) {
            customer = customerRepository.findBySessionId(request.getCustomerSessionId())
                    .orElse(createNewCustomer(business, request.getIpAddress()));
        } else {
            customer = createNewCustomer(business, request.getIpAddress());
        }

        // Create new chat session
        ChatSession session = ChatSession.builder()
                .business(business)
                .customer(customer)
                .status("ACTIVE")
                .build();

        return chatSessionRepository.save(session);
    }

    private Customer createNewCustomer(Business business, String ipAddress) {
        Customer customer = Customer.builder()
                .business(business)
                .sessionId(UUID.randomUUID().toString())
                .ipAddress(ipAddress)
                .build();
        return customerRepository.save(customer);
    }

    public ChatResponseDTO sendMessage(Long sessionId, ChatRequestDTO request) {
        // Get session
        ChatSession session = chatSessionRepository.findById(sessionId)
                .orElseThrow(() -> new ResourceNotFoundException("Chat session not found"));

        // Update customer info if provided
        if (request.getCustomerName() != null || request.getCustomerEmail() != null) {
            Customer customer = session.getCustomer();
            if (request.getCustomerName() != null) {
                customer.setName(request.getCustomerName());
            }
            if (request.getCustomerEmail() != null) {
                customer.setEmail(request.getCustomerEmail());
            }
            customerRepository.save(customer);
        }

        // Save user message
        Message userMessage = Message.builder()
                .chatSession(session)
                .sender("user")
                .content(request.getContent())
                .timestamp(LocalDateTime.now())
                .build();
        messageRepository.save(userMessage);

        // Get conversation history
        List<Message> history = messageRepository.findByChatSessionIdOrderByTimestampAsc(sessionId);

        // Generate AI response
        String systemPrompt = session.getBusiness().getAgentSystemPrompt();
        String aiResponseContent = aiService.generateResponse(systemPrompt, history, null);

        // Save AI response
        Message aiMessage = Message.builder()
                .chatSession(session)
                .sender("assistant")
                .content(aiResponseContent)
                .timestamp(LocalDateTime.now())
                .build();
        messageRepository.save(aiMessage);

        // Try to extract lead information
        leadService.tryExtractLead(session, history);

        return ChatResponseDTO.builder()
                .sessionId(sessionId)
                .message(aiResponseContent)
                .sender("assistant")
                .timestamp(aiMessage.getTimestamp())
                .build();
    }

    public void endSession(Long sessionId) {
        ChatSession session = chatSessionRepository.findById(sessionId)
                .orElseThrow(() -> new ResourceNotFoundException("Chat session not found"));
        
        session.setStatus("ENDED");
        session.setEndedAt(LocalDateTime.now());
        chatSessionRepository.save(session);
    }



    public List<MessageDTO> getSessionHistory(Long sessionId) {

        if (!chatSessionRepository.existsById(sessionId)) {
            throw new ResourceNotFoundException("Chat session not found");
        }

        return messageRepository
                .findByChatSessionIdOrderByTimestampAsc(sessionId)
                .stream()
                .map(message -> MessageDTO.builder()
                        .id(message.getId())
                        .sender(message.getSender())
                        .content(message.getContent())
                        .timestamp(message.getTimestamp())
                        .build())
                .toList();
    }

    public List<ChatSessionDTO> getBusinessSessions(Long businessId) {

        return chatSessionRepository
                .findByBusinessIdOrderByStartedAtDesc(businessId)
                .stream()
                .map(session -> ChatSessionDTO.builder()
                        .sessionId(session.getId())
                        .customerId(session.getCustomer().getId())
                        .customerSessionId(
                                session.getCustomer().getSessionId())
                        .status(session.getStatus())
                        .startedAt(session.getStartedAt())
                        .endedAt(session.getEndedAt())
                        .build())
                .toList();
    }
}
