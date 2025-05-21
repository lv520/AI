package com.lvhui.lvaiagent.app;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class LoveAppTest {
    @Resource
    private LoveApp loveApp;
    @Test
    void dochat() {
        String chatId = UUID.randomUUID().toString();
        String message = "你好，我是程序员 小辉";
        String answer = loveApp.dochat(message,chatId);
        message = "我喜欢 小美，我该怎么办";
        answer = loveApp.dochat(message,chatId);
        Assertions.assertNotNull(answer);
        message = "我喜欢的是谁来着，告诉我她的名字叫什么";
        answer = loveApp.dochat(message,chatId);
        Assertions.assertNotNull(answer);
    }

    @Test
    void doChatWithReport() {
        String chatId = UUID.randomUUID().toString();
        // 第一轮
        String message = "你好，我是程序员小辉，我想让另一半更爱我，但我不知道该怎么做";
        LoveApp.LoveReport loveReport = loveApp.doChatWithReport(message, chatId);
        Assertions.assertNotNull(loveReport);
    }

    @Test
    void doChatWithRAG() {
        String chatId = UUID.randomUUID().toString();
        // 第一轮
        String message = "你好，我是程序员小辉，婚后产生矛盾我该怎么办";
        String answer = loveApp.doChatWithRAG(message, chatId);
        Assertions.assertNotNull(answer);
    }
}