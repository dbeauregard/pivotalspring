package io.dbeauregard.pivotalspring.ai;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.PromptChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.QuestionAnswerAdvisor;
import org.springframework.ai.chat.memory.InMemoryChatMemory;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@Profile("ai")
public class OllamaClientController {

    private final ChatClient chatClient;
    private static final Logger log = LoggerFactory.getLogger(OllamaClientController.class);
    private final VectorStore vectorStore;

    public OllamaClientController(ChatClient.Builder builder, VectorStore vectorStore) {
        this.vectorStore = vectorStore;
        PromptChatMemoryAdvisor memory = new PromptChatMemoryAdvisor(new InMemoryChatMemory());
        this.chatClient = builder.defaultSystem("""
                You are a helpful assistant.
                """)
                .defaultAdvisors(memory)
                .defaultAdvisors(new QuestionAnswerAdvisor(vectorStore))
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

    // @ModelAttribute("allFeatures")
    // public List<Message> populateFeatures() {
    // return Arrays.asList(Message.ALL);
    // }

    @GetMapping("/ai")
    public String getAI(@RequestParam(name = "message", required = false) String msg,
            Model model)
            throws Exception {
        log.info("AI Called with: {}", msg);

        // Only call Model if a message is suplied, else respond with default response
        String result = "Type you message above and click submit.";
        if (msg != null) {
            result = chatClient.prompt().user(msg).call().content(); // .advisor()
            log.info("AI Result: {}", result);
        }

        model.addAttribute("result", result);
        model.addAttribute("message", new Message());
        return "ai";
    }
}
