package io.dbeauregard.pivotalspring.ai;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Configuration
public class OllamaClient {

    private final OllamaChatModel chatModel;
    private static final Logger log = LoggerFactory.getLogger(OllamaClient.class);

    @Autowired
    public OllamaClient(OllamaChatModel chatModel) {
        this.chatModel = chatModel;
    }

    // @Bean
    // @Profile("!test")
    public CommandLineRunner run() throws Exception {
        return args -> {
            var result = chatModel.call("What is Spring?");
            log.info("AI Result: {}", result);
        };
    }
}
