package io.dbeauregard.pivotalspring.ai;

import java.util.List;
import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.PromptChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.QuestionAnswerAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.memory.InMemoryChatMemory;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.TextReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.Resource;

import io.dbeauregard.pivotalspring.HouseEntity;
import io.dbeauregard.pivotalspring.HouseRepository;

@Configuration
@Profile("ai")
public class OllamaClientConfig {

    private ChatClient.Builder builder;
    private ChatClient chatClient;
    private static final Logger log = LoggerFactory.getLogger(OllamaClientConfig.class);
    private final VectorStore vectorStore;
    private final HouseRepository repo;

    @Value("${io.dbeauregard.pivotalspring.baseprompt}")
    private String basePrompt;

    @Value("${io.dbeauregard.pivotalspring.ragprompt}")
    private String ragPrompt;

    @Value("${io.dbeauregard.pivotalspring.enablerag}")
    private Boolean enablerag;

    @Value("${io.dbeauregard.pivotalspring.enablefunctions}")
    private Boolean enableFunctions;

    @Value("classpath:spring-rag.txt")
    private Resource springRagDoc;

    public OllamaClientConfig(ChatClient.Builder builder, VectorStore vectorStore, HouseRepository repo) {
        this.vectorStore = vectorStore;
        this.builder = builder;
        this.repo = repo;
    }

    private void buildChatClient() {
        if (chatClient != null)
            return; // already exists

        PromptChatMemoryAdvisor memory = new PromptChatMemoryAdvisor(new InMemoryChatMemory());
        builder = builder.defaultSystem(basePrompt) // Prompt
                .defaultAdvisors(memory); // Chat Memory

        // RAG
        if (enablerag) {
            loadEmbeddings();
            builder = builder.defaultAdvisors(new QuestionAnswerAdvisor(vectorStore));// SearchRequest.defaults(),
                                                                                      // ragPrompt),
                                                                                      // //RAG
        }

        // Functions
        if (enableFunctions)
            builder = builder.defaultFunctions("getHouses"); // Function

        builder = builder.defaultAdvisors(new SimpleLoggerAdvisor()); // Logging, "add toward end"
        this.chatClient = builder.build();

    }

    private void loadEmbeddings() {
        // Add the documents to PGVector
        vectorStore.write(
                new TokenTextSplitter().transform(
                        new TextReader(springRagDoc).read()));

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
            log.info("My AI Function Called with. {}", Reqeust);
            return repo.findAll();
        };
    }

    record Reqeust(String input) {
    }

}
