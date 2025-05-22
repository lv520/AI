package com.lvhui.lvaiagent.tools;


import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 调用url搜索
 */
public class WebSearchTool {

    // SearchAPI 的搜索接口地址
    private static final String SEARCH_API_URL = "https://www.searchapi.io/api/v1/search";

    private final String apiKey;

    public WebSearchTool(String apiKey) {
        this.apiKey = apiKey;
    }

    @Tool(description = "Search for information from Baidu Search Engine")
    public String searchWeb(
            @ToolParam(description = "Search query keyword") String query) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("q", query);
        paramMap.put("api_key", apiKey);
        paramMap.put("engine", "baidu");
        try {
            String response = HttpUtil.get(SEARCH_API_URL, paramMap);
            // 解析返回的 JSON 数据
            JSONObject jsonObject = JSONUtil.parseObj(response);
            // 提取 organic_results 部分
            JSONArray organicResults = jsonObject.getJSONArray("organic_results");

            // 检查 organicResults 是否为 null 或为空
            if (organicResults == null || organicResults.isEmpty()) {
                return "No search results found.";
            }

            // 确保不会超出 JSONArray 的范围
            int sublistSize = Math.min(organicResults.size(), 3);
            List<Object> objects = organicResults.subList(0, sublistSize);

            // 拼接搜索结果为字符串
            String result = objects.stream()
                    .map(obj -> {
                        JSONObject tmpJSONObject = (JSONObject) obj;
                        return tmpJSONObject.toString();
                    })
                    .collect(Collectors.joining(","));
            return result;
        } catch (Exception e) {
            return "Error searching Baidu: " + e.getMessage();
        }
    }
}
