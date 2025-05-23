package com.lvhui.lvaiagent.agent;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class LvManusTest {
    @Resource
    private LvManus lvManus;
    @Test
    public void test(){
        String userPrompt = """  
                我的另一半居住在南京夫子庙，请帮我找到 5 公里内合适的约会地点，  
                并结合一些网络图片，制定一份详细的约会计划，  
                并以 PDF 格式输出，图片需要加入pdf中""";
        String answer = lvManus.run(userPrompt);
        Assertions.assertNotNull(answer);
    }

}