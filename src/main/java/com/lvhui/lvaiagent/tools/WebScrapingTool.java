package com.lvhui.lvaiagent.tools;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;

import java.io.IOException;

/**
 * 网页内容抓取
 */
public class WebScrapingTool {
    @Tool(description = "Scrape the content from the url")
    public String scrapeWebPage(@ToolParam(description = "URL of the Web page to scrape") String url) {
        try {
            Document document = Jsoup.connect(url).get();
            return document.html();
        } catch (IOException e) {
            return "error:" + e.getMessage();
        }
    }
}
