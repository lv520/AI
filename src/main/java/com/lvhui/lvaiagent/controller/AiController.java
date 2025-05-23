package com.lvhui.lvaiagent.controller;

import com.lvhui.lvaiagent.agent.LvManus;
import com.lvhui.lvaiagent.app.LoveApp;
import jakarta.annotation.Resource;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/ai")
public class AiController {
    @Resource
    private LoveApp loveApp;

    /**
     * 同步调用 ai 恋爱大师应用
     * @param message
     * @param chatId
     * @return
     */
    @GetMapping("/love_app/chat/sync")
    public String doCharWithLoveAppSync(String message,String chatId){
        return loveApp.dochat(message,chatId);
    }

    /**
     * SSE流式调用 ai 恋爱大师应用(第一种方式)
     * @param message
     * @param chatId
     * @return
     */
    @GetMapping(value = "/love_app/chat/sse", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> doCharWithLoveAppSSE(String message, String chatId){
        return loveApp.dochatByStream(message,chatId);
    }


    /**
     * SSE流式调用 ai 恋爱大师应用(第二种方式)
     * @param message
     * @param chatId
     * @return
     */
    @GetMapping(value = "/love_app/chat/server_sent_event")
    public Flux<ServerSentEvent<String>> doChatWithLoveAppSeverSentEvent(String message, String chatId) {
        return loveApp.dochatByStream(message, chatId)
                .map(chunk -> ServerSentEvent.<String>builder()
                        .data(chunk)
                        .build());
    }

    @Resource
    private ToolCallback[] toolCallback;
    @Resource
    private ChatModel dashscopeChatModel;

    /**
     * 流式调用智能体
     * @param message
     * @return
     */
    @GetMapping(".manus.chat")
    public SseEmitter doChatWithManus(String message){
        LvManus lvManus = new LvManus(toolCallback, dashscopeChatModel);
        return lvManus.runStream(message);
    }

}
