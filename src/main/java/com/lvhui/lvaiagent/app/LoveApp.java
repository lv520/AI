package com.lvhui.lvaiagent.app;

import com.lvhui.lvaiagent.advisor.MyLoggerAdvisor;
import com.lvhui.lvaiagent.advisor.ReReadingAdvisor;
import com.lvhui.lvaiagent.chatmemory.FileBasedChatMemory;
import com.lvhui.lvaiagent.rag.LoveAppRagCustomAdvisorFactory;
import com.lvhui.lvaiagent.rag.QueryRewriter;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.QuestionAnswerAdvisor;
import org.springframework.ai.chat.client.advisor.api.Advisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.InMemoryChatMemory;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor.CHAT_MEMORY_CONVERSATION_ID_KEY;
import static org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor.CHAT_MEMORY_RETRIEVE_SIZE_KEY;

@Component
@Slf4j
public class LoveApp {
    private final ChatClient chatClient;
    private static final String SYSTEM_PROMPT = "你是一个恋爱专家，请根据用户输入，给出专业的恋爱建议。";

    /**
     * 初始化ai客户端
     * @param dashscopeChatModel
     */
    public LoveApp(ChatModel dashscopeChatModel) {
        // 初始化基于文件的对话记忆
        String fileDir = System.getProperty("user.dir") + "/tmp/chat-memory";
        ChatMemory chatMemory = new FileBasedChatMemory(fileDir);
        // 初始化基于内存的对话记忆
        // ChatMemory chatMemory = new InMemoryChatMemory();
        chatClient = ChatClient.builder(dashscopeChatModel)
                .defaultSystem(SYSTEM_PROMPT)
                .defaultAdvisors(
                        new MessageChatMemoryAdvisor(chatMemory),
                        // 自定义日志advisor
                        new MyLoggerAdvisor()
//                        // 自定义推理增强advisor
//                        new ReReadingAdvisor()
                )
                .build();
    }


    public String dochat(String message,String chatId){
        ChatResponse chatResponse = chatClient
                .prompt()
                .user(message)
                .advisors(spec->spec.param(CHAT_MEMORY_CONVERSATION_ID_KEY,chatId)
                        .param(CHAT_MEMORY_RETRIEVE_SIZE_KEY, 10))
                .call()
                .chatResponse();
        String content = chatResponse.getResult().getOutput().getText();
        log.info("content:{}",content);
        return content;
    }

    /**
     * 快速定义类
     * @param title
     * @param suggestions
     */
    record LoveReport(String title, List<String> suggestions) {
    }

    /**
     * 结构化输出
     * @param message
     * @param chatId
     * @return
     */
    public LoveReport doChatWithReport(String message, String chatId) {
        LoveReport loveReport = chatClient
                .prompt()
                .system(SYSTEM_PROMPT + "每次对话后都要生成恋爱结果，标题为{用户名}的恋爱报告，内容为建议列表")
                .user(message)
                .advisors(spec -> spec.param(CHAT_MEMORY_CONVERSATION_ID_KEY, chatId)
                        .param(CHAT_MEMORY_RETRIEVE_SIZE_KEY, 10))
                .call()
                .entity(LoveReport.class);
        log.info("loveReport: {}", loveReport);
        return loveReport;
    }
    // 恋爱ai 知识库rag功能

    @Resource
    private VectorStore loveAppVectorStore;
    @Resource
    private Advisor loveAppRagCloudAdvisor;
    @Resource
    private VectorStore pgVectorVectorStore;
    @Resource
    private QueryRewriter queryRewriter;
    /**
     * 本地rag功能
     * @param message
     * @param chatId
     */
    public String doChatWithRAG(String message, String chatId) {
        // 查询重写
        String rewriteMessage = queryRewriter.doQueryRewrite(message);
        ChatResponse chatResponse = chatClient
                .prompt()
                // 使用改写后的查询，凝练输入的提问
                .user(rewriteMessage)
                .advisors(spec -> spec.param(CHAT_MEMORY_CONVERSATION_ID_KEY, chatId)
                        .param(CHAT_MEMORY_RETRIEVE_SIZE_KEY, 10))
                // 开启日志
                //.advisors(new MyLoggerAdvisor())
                // 应用rag知识库问答（本地知识库）
                .advisors(new QuestionAnswerAdvisor(loveAppVectorStore))
                // 应用rag检索增强服务（基于云知识服务）
//                .advisors(loveAppRagCloudAdvisor)
                // 应用rag检索增强服务（基于pgvector）
                //.advisors(new QuestionAnswerAdvisor(pgVectorVectorStore))
                // 应用自定义的 rag 检索增强服务（文档查询 + 上下文增强 + 兜底）
//                .advisors(
//                        LoveAppRagCustomAdvisorFactory.createLoveAppRagCustomAdvisor(
//                                loveAppVectorStore,"已婚"
//                        )
//                )
                .call()
                .chatResponse();
        String content = chatResponse.getResult().getOutput().getText();
        log.info("content:{}",content);
        return content;
    }
}
