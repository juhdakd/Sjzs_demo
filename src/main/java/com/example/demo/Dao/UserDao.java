package com.example.demo.Dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import com.example.demo.Entity.User;

@Mapper
@Repository
public interface UserDao {

    @Select("select * from user")
    List<User> getAll();

}
