package com.example.demo.Controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.Entity.Result;
import com.example.demo.Service.MissionService;

import jakarta.annotation.Resource;

@CrossOrigin
@RestController
@RequestMapping("/mission")
public class MissionController {
    @Resource
    private MissionService missionService;

    @PostMapping("/input")
    public Result input(String MainMission){
        return Result.success(missionService.input(MainMission));
    }

}
