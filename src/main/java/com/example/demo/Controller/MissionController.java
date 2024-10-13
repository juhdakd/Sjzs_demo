package com.example.demo.Controller;

import java.io.IOException;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.Entity.Mission;
import com.example.demo.Entity.Result;
import com.example.demo.Entity.Submission;
import com.example.demo.Service.MissionService;

import jakarta.annotation.Resource;

@CrossOrigin
@RestController
@RequestMapping("/mission")
public class MissionController {
    @Resource
    private MissionService missionService;

    /*
     * 目前完成的任务：
     * 1、输入mision 将mission和大模型对接 并将返回的Submission存储到数据库中 展示
     * 2、更新mission 并将对应的Submission信息更新到数据库中
     * 3、删除mission 并将对应的Submission信息删除
     * 
     * 下一步工作：
     * 
     * 1、其他板块的对接
     * 2、map 路径 数据 的获取和存储
     * 
     */
    // 添加mission
    @PostMapping("/input")
    public Result input(String mission) {
        return Result.success(missionService.input(mission));
    }

    @PostMapping("/inputwithimage")
    public Result inputMissionWithImage(
            @RequestParam("mission") String mainMission,
            @RequestParam(value = "image", required = false) MultipartFile imageFile) {
        try {
            // Call service to process mission and image
            Submission submission = missionService.inputMissionWithImage(mainMission, imageFile);
            return Result.success(submission); // Wrap the result in a success response
        } catch (IOException e) {
            return Result.fail("Error processing mission with image: " + e.getMessage()); 
        }
    }
    // 获得所有输入的信息
    @GetMapping("/getAll")
    public Result getAll() {
        return Result.success(missionService.getAll());
    }

    // 更改mission
    @PostMapping("/update")
    public Result update(Mission mission) {
        return Result.success(missionService.update(mission));
    }

    // 删除mission
    @PostMapping("/delete")
    public Result delete(Mission mission) {
        return Result.success(missionService.delete(mission));
    }

    // 找到一个mission 和它所对应的任务以及所有的描述
    @PostMapping("/get")
    public Result get(Mission mission) {
        return Result.success(missionService.get(mission.getThread_id()));
    }

}
