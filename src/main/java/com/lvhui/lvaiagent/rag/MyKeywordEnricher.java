package com.lvhui.lvaiagent.rag;

import jakarta.annotation.Resource;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.document.Document;
import org.springframework.ai.transformer.KeywordMetadataEnricher;
import org.springframework.retry.annotation.Recover;
import org.springframework.stereotype.Component;

import javax.print.Doc;
import java.util.List;

@Component
public class MyKeywordEnricher {
    @Resource
    private ChatModel dashscopeChatModel;
    public List<Document> enrichDocuments(List<Document> documents) {
        KeywordMetadataEnricher keywordMetadataEnricher = new KeywordMetadataEnricher(dashscopeChatModel,5);
        return keywordMetadataEnricher.apply(documents);
    }
}
