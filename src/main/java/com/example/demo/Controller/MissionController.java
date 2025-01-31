package com.example.demo.Controller;

import java.io.IOException;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.Entity.LLM_Response;
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
    // 添加mission
    @PostMapping("/input")
    public Result input(Mission mission,boolean is_submit) {
        return Result.success(missionService.input(mission,is_submit));
    }

    // 处理带有图片的请求
    @PostMapping("/inputwithimage")
    public Result inputMissionWithImage(
            @RequestParam Mission mission,
            @RequestParam boolean is_submit,
            @RequestParam(value = "image", required = false) MultipartFile imageFile) {
        try {
            // 调用 service 处理 mission 和 image
            Submission submission = missionService.inputMissionWithImage(mission, is_submit,imageFile);
            return Result.success(submission);
        } catch (IOException e) {
            return Result.fail("fail load image");
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
    // 获得紧急地图
    @PostMapping("/getEmer_Map")
    public Result getEmer_Map(Mission mission) {
        return Result.success(missionService.GetEmergency_Map(mission));
    }

    @PostMapping("/input")
    public Result input(@RequestBody LLM_Response request) {
        System.out.println(request.thread_id);
        System.out.println(request.text);
        String reString=missionService.LLM_Chat(request.thread_id, request.text);
        //String reString="好";
        return Result.success(reString);
    }
}
