package com.bcsdlab.internal.bbot.service;

import org.springframework.stereotype.Service;

@Service
public class MessageService {

    public void processMessage(String user, String text, String timestamp) {
        // 메시지 수집 로직 구현
        System.out.println("User: " + user + ", Text: " + text + ", Timestamp: " + timestamp);
    }
}
