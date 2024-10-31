package com.example.demo.Dao;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import com.example.demo.Entity.Mission;
import com.example.demo.Entity.Object_feature;
import com.example.demo.Entity.Returntype;
import com.example.demo.Entity.Submission;

@Mapper
@Repository
public interface MissionDao {
        @Insert("insert into mission(thread_id, text, mission_type, status, deadline, current_longitude, current_latitude, target_longitude, target_latitude, length, width) "
                        +
                        "values(#{thread_id}, #{text}, #{mission_type}, #{status}, #{deadline}, #{current_longitude}, #{current_latitude}, #{target_longitude}, #{target_latitude}, #{length}, #{width})")
        void input(Mission mission);

        @Insert("insert into submission(thread_id, fly_object, fly_type) values(#{thread_id},#{fly_object},#{fly_type})")
        void addSubmission(Submission submission);

        // 获得所有的输入信息 以及其所对应的task信息
        @Select("SELECT m.*, s.submission_id, s.fly_type, s.fly_object, c.name, c.value\r\n" + //
                        "FROM mission m \r\n" + //
                        "INNER JOIN submission s ON m.thread_id = s.thread_id\r\n" + //
                        "INNER JOIN object_feature c ON s.submission_id = c.submission_id\r\n" + //
                        "WHERE m.thread_id IN (SELECT thread_id FROM submission);")
        List<Returntype> getAll();

        @Update("update mission " +
                        "set text=#{text}, mission_type=#{mission_type}, status=#{status}, deadline=#{deadline}, current_longitude=#{current_longitude}, current_latitude=#{current_latitude}, "
                        +
                        "target_longitude=#{target_longitude}, target_latitude=#{target_latitude}, length=#{length}, width=#{width} "
                        +
                        "where thread_id=#{thread_id}")
        void updatemission(Mission mission);

        @Delete("delete from submission where thread_id=#{thread_id}")
        void deleteSubmission(Mission mission);

        @Delete("delete from mission where thread_id=#{thread_id}")
        void deletemission(Mission mission);

        @Select("select submission_id from submission where thread_id=#{thread_id}")
        int getsubmissionid(Submission submissionJson);

        @Insert("insert into object_feature(submission_id, name, value) values(#{submission_id},#{object_feature.name},#{object_feature.value})")
        void addFeature(int submission_id, Object_feature object_feature);

        @Select("  SELECT m.*, s.submission_id, s.fly_type, s.fly_object, c.name, c.value\r\n" + //
                        "        FROM mission m \r\n" + //
                        "        INNER JOIN submission s ON m.thread_id = s.thread_id\r\n" + //
                        "        INNER JOIN object_feature c ON s.submission_id = c.submission_id\r\n" + //
                        "        WHERE m.thread_id IN (SELECT thread_id FROM submission) and m.thread_id=#{thread_id}")
        List<Returntype> getmission(String thread_id);

        @Select("SELECT * FROM mission WHERE status = '执行中'")
        List<Mission> getActiveMissions();

        @Insert("insert into map(thread_id, map) values(#{thread_id},#{Map})")
        void inputMap(String thread_id, String Map);
}
