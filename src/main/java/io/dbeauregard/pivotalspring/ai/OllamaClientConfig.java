package io.dbeauregard.pivotalspring.ai;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.PromptChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.QuestionAnswerAdvisor;
import org.springframework.ai.chat.memory.InMemoryChatMemory;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import io.dbeauregard.pivotalspring.HouseEntity;
import io.dbeauregard.pivotalspring.HouseRepository;

@Configuration
@Profile("ai")
public class OllamaClientConfig {

    private final ChatClient.Builder builder;
    private ChatClient chatClient;
    private static final Logger log = LoggerFactory.getLogger(OllamaClientConfig.class);
    private final VectorStore vectorStore;
    private final HouseRepository repo;

    @Value("${io.dbeauregard.pivotalspring.baseprompt}")
    private String basePrompt;

    @Value("${io.dbeauregard.pivotalspring.ragprompt}")
    private String ragPrompt;

    public OllamaClientConfig(ChatClient.Builder builder, VectorStore vectorStore, HouseRepository repo) {
        this.vectorStore = vectorStore;
        this.builder = builder;
        this.repo = repo;
    }

    private void buildChatClient() {
        if(chatClient != null) return;

        PromptChatMemoryAdvisor memory = new PromptChatMemoryAdvisor(new InMemoryChatMemory());
        this.chatClient = builder
                .defaultSystem(basePrompt) // Prompt
                .defaultAdvisors(memory) // Chat Memory
                .defaultAdvisors(new QuestionAnswerAdvisor(vectorStore, SearchRequest.defaults(), ragPrompt)) // RAG
                .defaultFunctions("getHouses") //Function
                .build();

        List<Document> documents = List.of(
                new Document(
                        "Spring AI rocks!! Spring AI rocks!! Spring AI rocks!! Spring AI rocks!! Spring AI rocks!!",
                        Map.of("meta1", "meta1")),
                new Document("The World is Big and Salvation Lurks Around the Corner"),
                new Document("You walk forward facing the past and you turn back toward the future.",
                        Map.of("meta2", "meta2")));

        // Add the documents to PGVector
        vectorStore.add(documents);

        // Retrieve documents similar to a query
        List<Document> results = vectorStore.similaritySearch(SearchRequest.query("Spring").withTopK(5));
        log.info("VectorStore Result: {}", results);
    }

    @Bean
    ChatClient getChatClient() {
        buildChatClient();
        return this.chatClient;
    }

    @Bean
    @Description("This is a list of houses for sale")
    Function<Reqeust, Iterable<HouseEntity>> getHouses() {
        return Reqeust -> {
            log.info("Function Called with. {}", Reqeust);
            return repo.findAll();
        };
    }

    record Reqeust(String input) {
    }

}
