package com.example.demo.Controller;


import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.Entity.Result;
import com.example.demo.Service.UserService;

import jakarta.annotation.Resource;


@CrossOrigin
@RestController
@RequestMapping("/user")
public class UserController {
    @Resource
    private UserService userService;
    @GetMapping("")
    public Result getAll(){
        return Result.success(userService.getAll());
    }
}
