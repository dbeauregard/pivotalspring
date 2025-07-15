package io.dbeauregard.pivotalspring.ai;

import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.dbeauregard.pivotalspring.HouseEntity;
import io.dbeauregard.pivotalspring.HouseRepository;

@Service
public class ToolFunction {

    private final HouseRepository repo;
    private static final Logger log = LoggerFactory.getLogger(ToolFunction.class);

    ToolFunction(HouseRepository repo) {
        this.repo = repo;
    }

    @Tool(description = "Get the current date and time in the user's timezone")
    String getCurrentDateTime() {
        return LocalDateTime.now().atZone(LocaleContextHolder.getTimeZone().toZoneId()).toString();
    }

    @Tool(description = "Get a list of houses that are for sale")
    String getHouses(String prompt) {
        log.info("My AI Function Called with: {}", prompt);
        Iterable<HouseEntity> he = repo.findAll();
        ObjectMapper mapper = new ObjectMapper();
        try {
            String s = mapper.writeValueAsString(he);
            // log.info(s);
            return s;
        } catch (JsonProcessingException e) {
            log.error("Error prossing houses to json.", e);
            return "No houses found for sale";
        }
    };
}
