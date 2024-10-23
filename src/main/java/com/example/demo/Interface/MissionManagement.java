package com.example.demo.Interface;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


@Service
public class MissionManagement {
    private static MissionManagement instance;

    // 私有构造函数，防止直接实例化
    private MissionManagement() {}

    // 获取单例实例的方法
    public static synchronized MissionManagement getInstance() {
        if (instance == null) {
            instance = new MissionManagement();
        }
        return instance;
    }

    // 处理应急任务的方法
    public String handleEmergencyTask(double latitude, double longitude, double mapLength, double mapWidth, String time) {
        // 构造地图API的URL
        String url = String.format("https://yourmapapi.com/emergency?latitude=%f&longitude=%f&length=%f&width=%f&time=%s", 
                                    latitude, longitude, mapLength, mapWidth, time);
        
        // 发送POST请求（使用RestTemplate）
        RestTemplate restTemplate = new RestTemplate();
        String response = restTemplate.postForObject(url, null, String.class);
        
        return response; // 返回地图URL作为响应
    }

    // 处理常规任务的方法
    public String handleRegularTask(double latitude, double longitude, double mapLength, double mapWidth) {
        // 构造地图API的URL
        String url = String.format("https://yourmapapi.com/regular?latitude=%f&longitude=%f&length=%f&width=%f", 
                                    latitude, longitude, mapLength, mapWidth);
        
        // 发送POST请求（使用RestTemplate）
        RestTemplate restTemplate = new RestTemplate();
        String response = restTemplate.postForObject(url, null, String.class);
        
        return response; // 返回地图URL作为响应
    }
}
