package io.dbeauregard.pivotalspring.ai;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.beans.factory.annotation.Autowired;

import io.dbeauregard.pivotalspring.HouseRepository;

public class ToolFunction {

    @Autowired
    private HouseRepository repo;
    private static final Logger log = LoggerFactory.getLogger(ToolFunction.class);

    @Tool(description = "Get a list of houses for sale")
    Object getHouses(String reqeust) {
        log.info("My AI Function Called with. {}", reqeust);
        return repo.findAll();
    };
}
