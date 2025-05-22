package com.lvhui.lvaiagent.tools;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GetTimeLocationToolTest {

    @Test
    void getTimeLocation() {
        GetTimeLocationTool getTimeLocationTool = new GetTimeLocationTool();
        String result = getTimeLocationTool.getTimeLocation();
        Assertions.assertNotNull(result);
    }
}