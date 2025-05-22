package com.lvhui.lvaiagent.tools;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class WebSearchToolTest {
    @Value("${search-api.api-key}")
    private String searchApiKey;
    @Test
    void searchWeb() {
        WebSearchTool webSearchTool = new WebSearchTool(searchApiKey);
        String query = "如何学习java";
        String result = webSearchTool.searchWeb(query);
        Assertions.assertNotNull(result);
    }
}