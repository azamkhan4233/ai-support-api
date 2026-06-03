package com.aisupport.api.service;

import com.aisupport.api.config.OpenAIConfig;
import com.aisupport.api.model.Message;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AIService {

    @Autowired
    private OpenAIConfig openAIConfig;

    @Autowired
    private OkHttpClient httpClient;

    @Value("${openai.model:llama-3.3-70b-versatile}")
    private String model;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public String generateResponse(String systemPrompt, List<Message> conversationHistory, String userMessage) {
        try {
            // Build messages array
            List<Map<String, String>> messages = new ArrayList<>();

            // Add system prompt
            if (systemPrompt != null && !systemPrompt.isEmpty()) {
                messages.add(Map.of("role", "system", "content", systemPrompt));
            } else {
                messages.add(Map.of("role", "system", "content", 
                    "You are a helpful customer support assistant. Be friendly, professional, and concise."));
            }

            // Add conversation history
            if (conversationHistory != null) {
                for (Message msg : conversationHistory) {
                    messages.add(Map.of(
                        "role", msg.getSender().equals("assistant") ? "assistant" : "user",
                        "content", msg.getContent()
                    ));
                }
            }

            // Add current user message (if not already in history)
            if (userMessage != null && !userMessage.isEmpty()) {
                messages.add(Map.of("role", "user", "content", userMessage));
            }

            // Build request body
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("model", model);
            requestBody.put("messages", messages);
            requestBody.put("temperature", 0.7);
            requestBody.put("max_tokens", 500);

            String jsonBody = objectMapper.writeValueAsString(requestBody);

            // Make API call
            Request request = new Request.Builder()
                    .url(openAIConfig.getApiUrl())
                    .addHeader("Authorization", "Bearer " + openAIConfig.getApiKey())
                    .addHeader("Content-Type", "application/json")
                    .post(RequestBody.create(jsonBody, MediaType.parse("application/json")))
                    .build();

            try (Response response = httpClient.newCall(request).execute()) {
                if (!response.isSuccessful()) {
                    throw new IOException("Unexpected response code: " + response);
                }

                String responseBody = response.body().string();
                JsonNode jsonResponse = objectMapper.readTree(responseBody);
                
                return jsonResponse
                        .path("choices")
                        .get(0)
                        .path("message")
                        .path("content")
                        .asText();
            }

        } catch (Exception e) {
            e.printStackTrace();
            return "I apologize, but I'm having trouble processing your request right now. Please try again.";
        }
    }

    public Map<String, String> extractLeadInfo(List<Message> conversationHistory) {
        try {
            // Build conversation context
            StringBuilder conversation = new StringBuilder();
            for (Message msg : conversationHistory) {
                conversation.append(msg.getSender()).append(": ").append(msg.getContent()).append("\n");
            }

            String extractionPrompt = "Extract the following information from this conversation if available: " +
                    "name, email, phone, interest/inquiry. Return ONLY a valid JSON object with these fields. " +
                    "If a field is not found, use null. Do not include any other text.\n\nConversation:\n" + 
                    conversation.toString();

            List<Map<String, String>> messages = List.of(
                Map.of("role", "system", "content", "You are a data extraction assistant. Extract lead information and return only valid JSON."),
                Map.of("role", "user", "content", extractionPrompt)
            );

            Map<String, Object> requestBody = Map.of(
                "model", model,
                "messages", messages,
                "temperature", 0.3,
                "max_tokens", 200
            );

            String jsonBody = objectMapper.writeValueAsString(requestBody);

            Request request = new Request.Builder()
                    .url(openAIConfig.getApiUrl())
                    .addHeader("Authorization", "Bearer " + openAIConfig.getApiKey())
                    .addHeader("Content-Type", "application/json")
                    .post(RequestBody.create(jsonBody, MediaType.parse("application/json")))
                    .build();

            try (Response response = httpClient.newCall(request).execute()) {
                if (response.isSuccessful()) {
                    String responseBody = response.body().string();
                    JsonNode jsonResponse = objectMapper.readTree(responseBody);
                    String content = jsonResponse.path("choices").get(0).path("message").path("content").asText();
                    
                    // Parse the extracted JSON
                    return objectMapper.readValue(content, Map.class);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return new HashMap<>();
    }
}
