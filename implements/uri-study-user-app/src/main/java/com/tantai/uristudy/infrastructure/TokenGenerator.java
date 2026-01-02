package com.tantai.uristudy.infrastructure;

import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class TokenGenerator {
    public String generateToken(){
        return UUID.randomUUID().toString();
    }
}
