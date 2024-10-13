package com.example.demo.Interface;

import com.example.demo.Entity.Mission;
import com.example.demo.Entity.Object_feature;
import com.example.demo.Entity.Submission;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.MediaType;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;



@Service
public class LLMInterface {

    private static final String URL = "http://localhost:8000/masifan/v1";

    private static final String IMAGE_URL = "http://localhost:8000/masifan/v2";
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
        // // 将 Submission 对象转换为 JSON 格式字符串并返回
        // String jsonResponse = "";
        // try {
        // jsonResponse = objectMapper.writeValueAsString(submission);
        // } catch (JsonProcessingException e) {
        // e.printStackTrace();
        // }

        // return jsonResponse;
    }

    // 处理带有图片的返回值
    // 发送 multipart/form-data 请求，包含 Mission 对象和图片
    public Submission sendPostRequestWithImage(Mission mission, MultipartFile imageFile) {
        RestTemplate restTemplate = new RestTemplate();

        // 设置请求头
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        headers.add("accept", "application/json");

        // 创建 MultipartBodyBuilder
        MultipartBodyBuilder builder = new MultipartBodyBuilder();
        builder.part("mission", mission);
        builder.part("image", imageFile.getResource());

        MultiValueMap<String, HttpEntity<?>> multipartRequest = builder.build();

        // 创建 HttpEntity
        HttpEntity<MultiValueMap<String, HttpEntity<?>>> requestEntity = new HttpEntity<>(multipartRequest, headers);

        // 发送POST请求并获取响应
        ResponseEntity<String> response = restTemplate.exchange(
                IMAGE_URL, HttpMethod.POST, requestEntity, String.class);

        // 将返回结果处理为 Submission 对象
        String responseBody = response.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        Submission submission = new Submission();
        submission.setThread_id(mission.getThread_id());

        try {
            JsonNode jsonNode = objectMapper.readTree(responseBody);
            if (jsonNode.has("task")) {
                JsonNode taskNode = jsonNode.get("task");
                submission.setFly_type(taskNode.get("fly_type").asText());
                submission.setFly_object(taskNode.get("fly_object").asText());

                JsonNode flyObjectFeatureNode = taskNode.get("fly_object_feature");
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
