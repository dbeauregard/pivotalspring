package io.dbeauregard.pivotalspring.ai;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import reactor.core.publisher.Flux;

@Controller
public class AIClientController {

    @Autowired(required = false) // Make this optional for testing and so page loads
    private ChatClient chatClient;
    private static final Logger log = LoggerFactory.getLogger(AIClientController.class);
    private static final String structuredDefault = "What alubms did the Smashing Pumpkins release.  Give me the top 10.";

    public AIClientController() {
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
            Assert.notNull(chatClient, "ChatClient not configured (null).");
            result = chatClient.prompt().user(msg).call().content();
            log.info("AI Result: {}", result);
        }

        model.addAttribute("result", result);
        model.addAttribute("message", new Message());
        return "ai";
    }

    @GetMapping("/ai/structured")
    public String getHouse(
            @RequestParam(name = "message", required = false, defaultValue = structuredDefault) String msg,
            Model model)
            throws Exception {

                log.info("AI Structured Output Called with: {}", msg);

        Assert.notNull(chatClient, "ChatClient not configured (null).");
        Album a = chatClient.prompt().user("What is the top selling Smashing Pumpkins Album?").call().entity(Album.class);
        model.addAttribute("result", a);
        // List<Album> al = chatClient.prompt().user(msg).call().entity(new ParameterizedTypeReference<List<Album>>() {});
        // if (al != null) {
        //     al.forEach(h -> log.info("AI Result: {}", h));
        //     model.addAttribute("result", al);
        // }

        model.addAttribute("message", new Message());
        return "ai";
    }

    @GetMapping("/ai/stream")
    public String getAiStreaming(@RequestParam(name = "message", required = false) String msg,
            Model model)
            throws Exception {
        log.info("AI Streaming Called with: {}", msg);

        // This wont work
        Flux<String> result = chatClient.prompt().user(msg).stream().content();

        model.addAttribute("result", result);
        model.addAttribute("message", new Message());
        return "ai";
    }

    record Album(String name, String Artist, String year) {
    };
}
