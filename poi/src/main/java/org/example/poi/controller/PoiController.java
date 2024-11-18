package org.example.poi.controller;

import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.hwpf.model.PicturesTable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/poi")
@Api(tags = {"excel文档操作"})
@Slf4j
public class PoiController {

    /**
     * 识别excel
     */
    @PostMapping("/getAnswer")
    @ApiOperation("获取答案")
    public String getAnswer(@ApiParam("包含习题及答案的文档") @RequestParam MultipartFile file, @ApiParam(value = "需要解答的习题(支持多道)") @RequestParam(required = false) String str) {
        String QUESTION_PATTERN = "(?s)(\\d+\\.)(.*?)(（\\d*\\.?\\d+分）).*?(?:纠错)*正确答案 *([A-D,]+|正确|错误)您的答案";
        String strPATTERN = "(?s)(\\d+\\.)(.*?)(（\\d*\\.?\\d+分）)";

        Map<String, String> data = new HashMap<>();
        List<String> result = new ArrayList<>();

        if (file.isEmpty() || !file.getOriginalFilename().endsWith(".doc")) {
            throw new RuntimeException("请上传一个有效的 .doc 文件");
        }

        try (InputStream inputStream = file.getInputStream();
             HWPFDocument document = new HWPFDocument(inputStream);
             WordExtractor extractor = new WordExtractor(document)) {
            PicturesTable picturesTable = document.getPicturesTable();

            // 读取文件内容
            String text = extractor.getText();
            text = text.replaceAll("\\s", "").replaceAll("\\n", "");

            String s = str.replaceAll("\\n", "");


            Pattern pattern = Pattern.compile(QUESTION_PATTERN);
            Matcher matcher = pattern.matcher(text);
            while (matcher.find()) {
                try {
                    String questionNumber = matcher.group(1); // 题号
                    String questionContent = matcher.group(2); // 题干
                    String score = matcher.group(3);   // 分数
                    String correctAnswer = matcher.group(4).replaceAll("\\s+", ""); // 正确答案（去除空格）
//                    System.out.println("题号: " + questionNumber);
//                    System.out.println("题干: " + questionContent);
//                    System.out.println("正确答案: " + correctAnswer);
//                    System.out.println("---------------------------");
                    String cleanedText = questionContent.replaceAll("[\\p{Punct}\\p{IsPunctuation}]", "");
                    if (StrUtil.isNotBlank(cleanedText)) {
                        data.put(cleanedText, correctAnswer);
                    }


                } catch (Exception e) {
                    log.error(e.getMessage());
                }

            }

            pattern = Pattern.compile(strPATTERN);
            matcher = pattern.matcher(s);
            while (matcher.find()) {
                try {
                    String questionNumber = matcher.group(1); // 题号
                    String questionContent = matcher.group(2); // 题干
//                    String score = matcher.group(3);   // 分数
//                    String correctAnswer = matcher.group(4).replaceAll("\\s+", ""); // 正确答案（去除空格）
//                    System.out.println("题号: " + questionNumber);
//                    System.out.println("题干: " + questionContent);
//                    System.out.println("正确答案: " + correctAnswer);
                    System.out.println("---------------------------");
//                    data.put(questionContent)
                    String cleanedText = questionContent.replaceAll("[\\p{Punct}\\p{IsPunctuation}]", "");
                    cleanedText = cleanedText.replaceAll("\\s+", "");
                    String finalCleanedText = cleanedText;
                    data.keySet().forEach(d -> {
                        if (StrUtil.isNotBlank(d) && StrUtil.isNotBlank(finalCleanedText) && d.contains(finalCleanedText) || finalCleanedText.contains(d)) {
                            String a = questionNumber + data.get(d);
                            result.add(a);
                            System.out.println("题号: " + questionNumber);
                            System.out.println("题干: " + questionContent);
                            System.out.println("答案: " + data.get(d));
                            System.out.println("---------------------------");
                        }
                    });


                } catch (Exception e) {
                    log.error(e.getMessage());
                }

            }
            List<String> re = new ArrayList<>();

            AtomicReference<StringBuilder> stringBuilder = new AtomicReference<>(new StringBuilder());

            result.forEach(a -> {
                String[] split = a.split("\\.");
                Integer i = Integer.valueOf(split[0]);
                String k = split[1];
                if (i % 5 == 1) {
                    stringBuilder.get().append("\n");
                    if (i == 1) {
                        stringBuilder.get().append("  ");
                    } else if (i == 6) {
                        stringBuilder.get().append(" ");
                    }

                    stringBuilder.get().append(i).append("-").append(i + 4).append(": ").append(k);
                } else {
                    stringBuilder.get().append(" ").append(k);
                }

            });
            return stringBuilder.toString();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

}
