package io.dbeauregard.pivotalspring.ai;

import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


@Controller
public class OllamaClientController {

    private final OllamaChatModel chatModel;
    private static final Logger log = LoggerFactory.getLogger(OllamaClientController.class);

    public OllamaClientController(OllamaChatModel chatModel) {
        this.chatModel = chatModel;
    }

    // @ModelAttribute("allFeatures")
    // public List<Message> populateFeatures() {
    //     return Arrays.asList(Message.ALL);
    // }

    @GetMapping("/ai")
    public String getAI(@RequestParam(name = "message", required = false, defaultValue = "What can I ask?") String msg,
            Model model)
            throws Exception {
        log.info("AI Called with: {}", msg);
        
        String result = """
                My Mock result
                a
                b
                c
                """;
        result = chatModel.call(msg);
        
        log.info("AI Result: {}", result);
        model.addAttribute("result", result);
        model.addAttribute("message", new Message());
        return "ai";
    }
}
