package com.airwallex.codechallenge;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class Writer {
  private static ObjectMapper objectMapper = new ObjectMapper();
  private static JavaTimeModule javaTimeModule = new JavaTimeModule();

  public Writer() {
    objectMapper.registerModule(javaTimeModule);
  }

  public String toJSONString(Object obj) throws JsonProcessingException {
    return objectMapper.writeValueAsString(obj);
  }
}
