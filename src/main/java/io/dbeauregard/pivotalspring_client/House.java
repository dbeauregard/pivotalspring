package io.dbeauregard.pivotalspring_client;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record House(Long id, String address, Long price) {}
