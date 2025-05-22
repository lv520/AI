package com.lvhui.lvaiagent.tools;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONUtil;
import org.springframework.ai.tool.annotation.Tool;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

public class GetTimeLocationTool {
    @Tool(description = "Get user's time and location")
    public String getTimeLocation(){
        String result = "";
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String location = "";
        String ip = "";
        try {
            // 获取本机IP地址
            InetAddress addr = InetAddress.getLocalHost();
            String ipAddress = addr.getHostAddress();
            ip= ipAddress;
            // 使用IP定位API获取地理位置信息
            String apiUrl = "https://ipinfo.io/" + ipAddress + "/json";
            HttpResponse response = HttpRequest.get(apiUrl).execute();

            if (response.isOk()) {
                Map<String, Object> temp = JSONUtil.toBean(response.body(), Map.class);
                location = temp.getOrDefault("city", "Unknown City") + ", " +
                        temp.getOrDefault("region", "Unknown Region") + ", " +
                        temp.getOrDefault("country", "Unknown Country");

            } else {
                location =  "Failed to get location: " + response.getStatus();
            }
        } catch (UnknownHostException e) {
            location = "Error getting IP address: " + e.getMessage();
        } catch (Exception e) {
            location = "Error getting location: " + e.getMessage();
        }
        result = now.format(formatter) + " " + location;
        return result;
    }
}
