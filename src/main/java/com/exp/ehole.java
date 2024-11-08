package com.exp;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
public class ehole {

    private String eholePath;

    public ehole(String eholePath) {
        this.eholePath = eholePath;
    }

    public String executeCommand(String url) {
        StringBuilder output = new StringBuilder();
        try {
            // 构造命令
            ProcessBuilder processBuilder = new ProcessBuilder(eholePath, "finger", "-u", url);
            System.out.println("执行命令: " + eholePath + " finger -u " + url);
            processBuilder.redirectErrorStream(true); // 合并标准输出和错误输出
            Process process = processBuilder.start();

            // 读取输出
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream(), StandardCharsets.UTF_8))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    output.append(line).append("\n");
                }
            }

            // 等待命令执行完成
            process.waitFor();
            System.out.println(output);

        } catch (Exception e) {
            e.printStackTrace();
            return "执行命令时出错: " + e.getMessage();
        }
        String targetPart = getLastPartFromOutput(output.toString());
        System.out.println("URL: " + url +"指纹:"+ targetPart);
        return targetPart;
    }

    public static String getLastPartFromOutput(String commandOutput) {
        // 更新正则表达式以适应输出格式
        String is_target = "\\[ https?://[^\\s]+ \\| ([^|]+) \\| [^|]+ \\| \\d+ \\| \\d+ \\| [^\\]]* \\]";
        Matcher matcher = Pattern.compile(is_target).matcher(commandOutput);
        StringBuilder result = new StringBuilder();

        if (matcher.find()) {
            result.append(matcher.group(1)).append("\n"); // 提取第一个捕获组
        }

        return result.length() > 0 ? result.toString().trim() : "";
    }
}
