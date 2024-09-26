package io.dbeauregard.pivotalspring.ai;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class OllamaClientController {

    private final ChatClient chatClient;
    private static final Logger log = LoggerFactory.getLogger(OllamaClientController.class);

    public OllamaClientController(ChatClient.Builder builder) {
        this.chatClient = builder.defaultSystem("talk like a drunken pirate").build();
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
            result = chatClient.prompt().user(msg).call().content();
            log.info("AI Result: {}", result);
        }

        model.addAttribute("result", result);
        model.addAttribute("message", new Message());
        return "ai";
    }
}
