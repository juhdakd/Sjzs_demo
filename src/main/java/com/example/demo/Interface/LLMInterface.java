package com.example.demo.Interface;

import com.example.demo.Entity.Mission;
import com.example.demo.Entity.Object_feature;
import com.example.demo.Entity.Submission;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.http.MediaType;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.io.File;

@CrossOrigin
@Service
public class LLMInterface {

    private static final String URL = "http://localhost:8000/masifan/v1";

    public Submission sendPostRequest(Mission mission) {
        RestTemplate restTemplate = new RestTemplate();

        // 设置请求头
        HttpHeaders headers = new HttpHeaders();
        headers.add("accept", "application/json");
        headers.add("Content-Type", "application/json");

        // 创建HttpEntity，包含输入的Mission对象
        HttpEntity<Mission> requestEntity = new HttpEntity<>(mission, headers);

        // 发送POST请求并获取响应
        ResponseEntity<String> response = restTemplate.exchange(
                URL, HttpMethod.POST, requestEntity, String.class);

        // 将返回结果处理为 Submission 对象
        String responseBody = response.getBody();

        ObjectMapper objectMapper = new ObjectMapper();
        Submission submission = new Submission();
        submission.setThread_id(mission.getThread_id());

        try {
            JsonNode jsonNode = objectMapper.readTree(responseBody);

            // 处理返回的任务信息
            if (jsonNode.has("task")) {
                JsonNode taskNode = jsonNode.get("task");

                String flyType = taskNode.get("fly_type").asText();
                String flyObject = taskNode.get("fly_object").asText();
                JsonNode flyObjectFeatureNode = taskNode.get("fly_object_feature");
                // 设置 Submission 对象的属性
                submission.setFly_type(flyType);
                submission.setFly_object(flyObject);

                // 处理 fly_object_feature
                List<Object_feature> objectFeatures = new ArrayList<>();
                if (flyObjectFeatureNode != null && flyObjectFeatureNode.isArray()) {
                    for (JsonNode featureNode : flyObjectFeatureNode) {
                        Object_feature feature = new Object_feature();
                        feature.setName(featureNode.get("name").asText());
                        feature.setValue(featureNode.get("value").asText());
                        objectFeatures.add(feature);
                    }
                }
                submission.setFly_object_feature(objectFeatures);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.err.println("测试:" + submission);
        return submission;
    }

    public Submission sendPostRequestWithImage(Mission mission, String imagePath) throws IOException {
        RestTemplate restTemplate = new RestTemplate();

        // 构建带查询参数的 URL
        String url = UriComponentsBuilder.fromHttpUrl("http://localhost:8000/masifan/v2")
                .queryParam("thread_id", mission.getThread_id())
                .queryParam("text", mission.getText())
                .toUriString();

        // 设置请求头
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        headers.add("accept", "application/json");

        // 创建 MultiValueMap 发送图片
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();

        // 如果有图片文件路径，读取文件并添加到请求体中
        if (imagePath != null && !imagePath.isEmpty()) {
            File imageFile = new File(imagePath);
            if (imageFile.exists() && imageFile.isFile()) {
                FileSystemResource imageResource = new FileSystemResource(imageFile);

                // 添加文件到请求体，确保类型正确
                body.add("image_file", imageResource);
            } else {
                System.out.println("文件不存在或不是一个有效的文件: " + imagePath);
            }
        }

        // 构建 HttpEntity
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        // 调试输出，确认请求构建是否成功
        System.out.println("正在发送 POST 请求到: " + url);

        // 发送 POST 请求到外部接口
        ResponseEntity<String> response = restTemplate.exchange(
                url, HttpMethod.POST, requestEntity, String.class);

        // 处理返回的结果
        ObjectMapper objectMapper = new ObjectMapper();
        Submission submission = new Submission();
        submission.setThread_id(mission.getThread_id());

        try {
            // 获取 JSON 响应并解析
            JsonNode jsonNode = objectMapper.readTree(response.getBody());

            // 处理 JSON 响应并设置到 submission 对象
            if (jsonNode.has("task")) {
                JsonNode taskNode = jsonNode.get("task");

                submission.setFly_type(taskNode.get("fly_type").asText());
                submission.setFly_object(taskNode.get("fly_object").asText());

                // 处理 fly_object_feature 列表
                List<Object_feature> objectFeatures = new ArrayList<>();
                JsonNode featureArray = taskNode.get("fly_object_feature");
                if (featureArray != null && featureArray.isArray()) {
                    for (JsonNode featureNode : featureArray) {
                        Object_feature feature = new Object_feature();
                        feature.setName(featureNode.get("name").asText());
                        feature.setValue(featureNode.get("value").asText());
                        objectFeatures.add(feature);
                    }
                }
                submission.setFly_object_feature(objectFeatures);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return submission;
    }

    // 示例代码
    public static void main(String[] args) {
        // 创建LLMInterface实例
        LLMInterface llmInterface = new LLMInterface();

        // 创建一个Mission对象并设置数据
        Mission mission = new Mission();
        mission.setThread_id("123");
        mission.setText("找一个黄色的小女孩");

        Submission submissionJson = llmInterface.sendPostRequest(mission);
        System.out.println(submissionJson);
    }
}
