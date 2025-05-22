package com.lvhui.lvaiagent.tools;

import cn.hutool.core.io.FileUtil;
import com.lvhui.lvaiagent.constant.FileConstant;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;

/**
 * 文件 操作 工具类（提取文件、读写文件）
 */
public class FileOperationTool {
    private final String File_DIR = FileConstant.FILE_SAVE_DIR + "/file";
    @Tool(description = "Read content from a file")
    public String readFile(@ToolParam(description = "Name of a file to read") String fileName){
        String filePath = File_DIR + "/" + fileName;
        try {
            return FileUtil.readUtf8String(filePath);
        }catch (Exception e){
            return "Error to read" + e.getMessage();
        }
    }
    @Tool(description = "Write content to a file")
    public String writeFile(@ToolParam(description = "Name of the file to write") String fileName,
                            @ToolParam(description = "Content to write to the file") String content
    ){
        String filePath = File_DIR + "/" + fileName;
        try {
            FileUtil.mkdir(File_DIR);
            FileUtil.writeString(content, filePath, "UTF-8");
            return "Write success:" + filePath;
        }catch (Exception e){
            return "Error to write" + e.getMessage();
        }
    }
}
