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
    public String getHouse(@RequestParam(name = "message", required = false) String msg,
            Model model)
            throws Exception {
        log.info("AI Structured Output Called with: {}", msg);

        // Only call Model if a message is suplied, else respond with default response
        String result = "Type you message above and click submit.";
        model.addAttribute("result", result);
        if (msg != null) {
            Assert.notNull(chatClient, "ChatClient not configured (null).");
            List<House> hl = chatClient.prompt().user(msg).call().entity(new ParameterizedTypeReference<List<House>>() {
            });
            if (hl != null) {
                hl.forEach(h -> log.info("AI Result: {}", h));
                model.addAttribute("result", hl);
            }
        }

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

    record House(String address, String price, String sqft, String bdrm, String bath) {
    };
}
