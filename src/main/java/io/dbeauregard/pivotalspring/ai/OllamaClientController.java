package io.dbeauregard.pivotalspring.ai;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OllamaClientController {

    private final OllamaChatModel chatModel;
    private static final Logger log = LoggerFactory.getLogger(OllamaClientController.class);

    public OllamaClientController(OllamaChatModel chatModel) {
        this.chatModel = chatModel;
    }

    @GetMapping("/ai")
    public String getAI(@RequestParam(name = "msg", required = false, defaultValue = "What can I ask?") String msg)
            throws Exception {
        String result = chatModel.call(msg);
        log.info("AI Result: {}", result);
        return result;
    }
}
