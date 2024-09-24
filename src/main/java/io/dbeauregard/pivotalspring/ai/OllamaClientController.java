package io.dbeauregard.pivotalspring.ai;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class OllamaClientController {

    private final OllamaChatModel chatModel;
    private static final Logger log = LoggerFactory.getLogger(OllamaClientController.class);

    public OllamaClientController(OllamaChatModel chatModel) {
        this.chatModel = chatModel;
    }

    @GetMapping("/ai")
    public String getAI(@RequestParam(name = "msg", required = false, defaultValue = "What can I ask?") String msg,
            Model model)
            throws Exception {
        log.info("AI Called with: {}", msg);
        String result = chatModel.call(msg);
        log.info("AI Result: {}", result);
        model.addAttribute("result", result);
        return "ai";
    }
}
