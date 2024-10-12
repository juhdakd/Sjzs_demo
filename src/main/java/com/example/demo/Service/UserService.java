package com.example.demo.Service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.Dao.UserDao;
import com.example.demo.Entity.User;

import jakarta.annotation.Resource;

@Service
public class UserService {
    @Resource
    private UserDao userDao;

    public List<User> getAll() {
        return userDao.getAll();
    }
}
