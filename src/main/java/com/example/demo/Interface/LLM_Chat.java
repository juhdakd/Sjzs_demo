package com.example.demo.Interface;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

@CrossOrigin
@Service
public class LLM_Chat {

    private static final String URL = "http://localhost:8000/masifan/v1"; // 替换为实际的URL

    public String sendPostRequest(String thread_id, String text) {
        RestTemplate restTemplate = new RestTemplate();

        System.out.println("Post:" + text);
        // 设置请求头
        HttpHeaders headers = new HttpHeaders();
        headers.add("accept", "application/json");
        headers.add("Content-Type", "application/json");

        // 创建请求体 JSON 对象
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode requestBody = objectMapper.createObjectNode();
        requestBody.put("thread_id", thread_id);
        requestBody.put("text", text);

        // 创建 HttpEntity，包含 JSON 请求体和头部
        HttpEntity<String> requestEntity = new HttpEntity<>(requestBody.toString(), headers);

        try {
            // 发送 POST 请求并获取响应
            ResponseEntity<String> response = restTemplate.exchange(
                    URL, HttpMethod.POST, requestEntity, String.class);

            // 解析响应体
            JsonNode rootNode = objectMapper.readTree(response.getBody());
            JsonNode messages = rootNode.path("messages");

            // 获取最后一个消息的 content 字段
            if (messages.isArray() && messages.size() > 0) {
                JsonNode lastMessage = messages.get(messages.size() - 1);
                JsonNode contentNode = lastMessage.get("content");
                return contentNode != null ? contentNode.asText() : "No content found";
            }

            return "No messages found";

        } catch (Exception e) {
            e.printStackTrace();
            return "Error occurred while processing the response.";
        }
    }
}
