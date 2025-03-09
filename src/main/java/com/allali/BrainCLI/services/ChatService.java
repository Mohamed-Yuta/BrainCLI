package com.allali.BrainCLI.services;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class ChatService {
    private final WebClient webClient;
    @Value("${openai.api.key}")
    private String apiKey;

    @Value("${openai.api.url}")
    private String apiUrl;
    public ChatService(
            WebClient.Builder webClientBuilder,
            @Value("${openai.api.url}") String apiUrl,
            @Value("${openai.api.key}") String apiKey) {

        this.webClient = webClientBuilder
                .baseUrl(apiUrl)
                .defaultHeader("Authorization", "Bearer " + apiKey)
                .build();
    }

    public Mono<String> askQuestion(String question) {
        String requestBody = String.format("""
                {
                    "model": "gpt-3.5-turbo",
                    "messages": [{"role": "user", "content": "%s"}]
                }
                """, question);

        return webClient.post()
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(String.class);
    }
}

