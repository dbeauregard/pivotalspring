package io.dbeauregard.pivotalspring.ai;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.PromptChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.chat.memory.InMemoryChatMemory;
import org.springframework.ai.document.Document;
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
            builder = builder.defaultAdvisors(new QuestionAnswerAdvisor(vectorStore, SearchRequest.builder().build(), ragPrompt));  //RAG
        }

        // Functions
        if (enableFunctions)
            builder = builder.defaultTools("getHouses"); // Function

        builder = builder.defaultAdvisors(new SimpleLoggerAdvisor()); // Logging, "add toward end"
        this.chatClient = builder.build();

    }

    private void loadEmbeddings() {
        // Add the documents to PGVector
        log.info("Loading Documents into VectorStore...");
        // vectorStore.write(new TokenTextSplitter().transform(new TextReader(springRagDoc).read()));
        List<Document> documents = List.of(
            new Document("Sears used to sell mail-order houses.  Available by catalog order from 1908 to 1940, Sears Modern Homes took DIY to a whole new level. As many as 75,000 home kits were sold in hundreds of different styles, some are still in existence.", Map.of("fact","sears")),
            new Document("A creaking house is related to temperature—not age. When it comes to houses, creaking and groaning aren’t necessarily signs of old age. House noises are usually caused by fluctuating humidity and temperature (also called thermal expansion and contraction).", Map.of("fact","creaking")),
            new Document("Homes with black front doors sell for the highest price.  A black front door is usually associated with the highest asking price, a Zillow survey noted. A home with a black front door might sell for up to $6,449 more, while a slate blue front door comes in close second.", Map.of("fact","door")),
            new Document("Off-white houses are the most popular.  Recently surveyed homeowners said lighter colors are better for a home’s exterior. A 2023 Alside poll found that off-white/cream is the preferred siding color, followed by light gray, white, and light brown.", Map.of("fact","paint")),
            new Document("Brass doorknobs can disinfect themselves.  The reason many doorknobs are made of brass is because of something known as the oligodynamic effect. Unvarnished brass objects can self-disinfect, proving especially useful on frequently touched surfaces.", Map.of("fact","doorknob")),
            new Document("The typical homeowner stays in their house for 13.2 years.  The number of years the typical homeowner in the U.S. spends in their house is 13.2 years. Homeowner tenure has risen from 10.1 years in 2012 and dropped from its highest recorded point of 13.5 years in 2020.", Map.of("fact","duration")),
            new Document("A seller must disclose if a house is haunted.  A 1991 Supreme Court decision—also referred to as the “Ghostbusters ruling” —helped drastically decrease the odds of accidentally buying a haunted house. Lack of disclosure could result in contract rescission.", Map.of("fact","haunted")),
            new Document("A castle in France may cost less than a teardown in Los Angeles.  You could become the proud owner of an 18th century French castle for a little over $1 million. For the same amount, you might not get much more than a home in need of an overhaul in L.A., depending on the market.", Map.of("fact","castle")),
            new Document("Property tax started in ancient times. It’s one of life’s not-so-fun facts: Taxation has always been a part of civilization. As for property taxes, there’s no one person to blame. Land taxes can be traced as far back as 5,000 B.C. in ancient Egypt.", Map.of("fact","tax")),
            new Document("The world’s biggest house is 400,000 square feet.  The biggest house in the world is owned by Mukesh Ambani and located in Antilia in Mumbai, India, according to the Guinness Book of World Records. It has 27 stories, multiple swimming pools, and 168 parking spaces.", Map.of("fact","biggest")),
            new Document("The world’s smallest house is 25 square feet.  The smallest house in the world manages to fit a sink, stove, and shower within its miniscule square footage. The tiny house, created by Glen Bunsen, is transportable and can fit inside a van.", Map.of("fact","smallest")),
            new Document("The most homes sold in one year is 6,438.  The record for selling the most homes in one year goes to Ben Caballero, a real estate agent in Dallas, Texas. Caballero sold more than 6,000 homes throughout 2020.", Map.of("fact","sold")),
            new Document("Homes are roughly 30 percent larger than they used to be.  Census data shows that the median size for a single-family home is 2,299 square feet. A median-sized home built before or during the 1960s measured at around 1,500 square feet.", Map.of("fact","size")),
            new Document("Mark Zuckerburg bought four houses surrounding his house.  Why would Facebook co-founder Mark Zuckerberg spend over $40 million to buy the four homes bordering his primary Palo Alto residence? Reportedly, for privacy—with the intention of creating his own compound.", Map.of("fact","zuck")),
            new Document("The first real estate agents were called 'curbstoners'.  A curbstoner typically refers to an unlicensed dealer, and in the early 1900s, this included real estate agents. At that time, curbstoners were said to be unregulated house flippers, hoping to quickly sell a property for cash.", Map.of("fact","curbstoner")),
            new Document("Homeowners used to burn their mortgage documents after paying off their house.  While Scottish homeowners might paint their front door red after making their last mortgage payment, early 20th century homeowners may have had a mortgage burning party instead.", Map.of("fact","mortgage")));
        vectorStore.add(documents);    

        // Retrieve documents similar to a query
        List<Document> results = vectorStore.similaritySearch(SearchRequest.builder().query("square feet size").topK(3).build());
        if(results != null) {
            results.forEach(r -> log.info("VectorStore Search Result: {}", r));
        }
    }

    @Bean
    ChatClient getChatClient() {
        buildChatClient();
        return this.chatClient;
    }

    @Bean
    @Description("Get a list of houses for sale")
    Function<Reqeust, Iterable<HouseEntity>> getHouses() {
        return Reqeust -> {
            log.info("My AI Function Called with. {}", Reqeust);
            return repo.findAll();
        };
    }

    record Reqeust(String input) {
    }

}
