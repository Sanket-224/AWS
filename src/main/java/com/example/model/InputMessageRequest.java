package com.example.model;

import lombok.Data;

@Data
public class InputMessageRequest {
    String message;
    String queueName;
}
