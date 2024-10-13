package com.example.demo.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.Dao.MissionDao;
import com.example.demo.Entity.Mission;
import com.example.demo.Entity.Object_feature;
import com.example.demo.Entity.Returntype;
import com.example.demo.Entity.Submission;
import com.example.demo.Interface.LLMInterface;

import jakarta.annotation.Resource;

@Service
public class MissionService {

    @Resource
    private MissionDao missionDao;

    @Autowired
    private LLMInterface llmInterface;

    public Object input(String mainMission) {
        // 使用时间生成一个特定的编号 时间戳
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyMMddHHmmss");
        String thread_id = now.format(formatter);

        // 将输入存入数据库
        Mission mission = new Mission();
        mission.setText(mainMission);
        mission.setThread_id(thread_id);
        missionDao.input(thread_id, mainMission);

        System.out.println("mission:" + mission.toString());
        Submission submissionJson = llmInterface.sendPostRequest(mission);

        // 将 Submission 数据插入数据库
        missionDao.addSubmission(submissionJson);
        // 获取生成的 submission_id
        int submissionJsonId = missionDao.getsubmissionid(submissionJson);
        System.out.println("测试：" + submissionJsonId);

        submissionJson.setSubmission_id(submissionJsonId);
        // 将 Object_feature 数据插入数据库
        for (Object_feature feature : submissionJson.getFly_object_feature()) {
            feature.setSubmission_id(submissionJsonId);
            missionDao.addFeature(submissionJsonId, feature);
        }

        return submissionJson;
    }

    public List<Returntype> getAll() {
        return missionDao.getAll();
    }

    // 更改=重新输入信息，update对应的text 并删除对应的submission 添加新的submission
    public Submission update(Mission mission) {
        // 处理新的输入信息
        // 更新mission的text
        missionDao.updatemission(mission);
        // 删除对应的submission
        missionDao.deleteSubmission(mission);
        // 将新的Submission信息存储到数据库
        Submission newsubmission = llmInterface.sendPostRequest(mission);
        missionDao.addSubmission(newsubmission);
        return newsubmission;
    }

    public Object delete(Mission mission) {
        // 删除mission
        missionDao.deletemission(mission);
        // 删除对应的submission
        missionDao.deleteSubmission(mission);
        return null;
    }

    public Submission get(String thread_id) {
        // 从数据库获取 mission 数据
        List<Returntype> listReturntypes = missionDao.getmission(thread_id);

        System.out.println("listReturntypes:" + listReturntypes.toString());
        // 创建一个 Submission 对象
        Submission submission = new Submission();

        // 假设第一个 Returntype 对象包含了 thread_id, fly_type 和 fly_object 的信息
        Returntype firstReturntype = listReturntypes.isEmpty() ? null : listReturntypes.get(0);
        if (firstReturntype != null) {
            submission.setSubmission_id(firstReturntype.getSubmission_id());
            submission.setThread_id(firstReturntype.getThread_id());
            submission.setFly_type(firstReturntype.getFly_type());
            submission.setFly_object(firstReturntype.getFly_object());
        }

        // 创建 Object_feature 列表
        // 创建 Object_feature 列表
        List<Object_feature> objectFeatures = new ArrayList<>();

        // 遍历 Returntype 列表，提取 name 和 value 并创建 Object_feature 对象
        for (Returntype returntype : listReturntypes) {
            Object_feature objectFeature = new Object_feature();
            objectFeature.setName(returntype.getName());
            objectFeature.setValue(returntype.getValue());
            objectFeatures.add(objectFeature);
        }
        // 设置 Object_feature 列表到 Submission 对象
        submission.setFly_object_feature(objectFeatures);

        // 返回 Submission 对象
        return submission;
    }


    public Submission inputMissionWithImage(String mainMission, MultipartFile imageFile) throws IOException {
        // Generate a unique thread ID using the current timestamp
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyMMddHHmmss");
        String thread_id = now.format(formatter);

        // Save the mission text to the database
        Mission mission = new Mission();
        mission.setText(mainMission);
        mission.setThread_id(thread_id);
        missionDao.input(thread_id, mainMission);

        System.out.println("Mission: " + mission.toString());

        // Handle the image upload if provided
        // if (imageFile != null && !imageFile.isEmpty()) {
        //     // Save the image to the file system or database
        //     // String imagePath = saveImage(imageFile);  // Implement image saving logic
        //     System.out.println("Image Path:);
        //     //mission.setImagePath(imagePath);  // Optionally store the image path in the mission
        // }

        // Send POST request and get submission response
        Submission submissionJson = llmInterface.sendPostRequestWithImage(mission, imageFile);

        // Save Submission to the database
        missionDao.addSubmission(submissionJson);
        int submissionJsonId = missionDao.getsubmissionid(submissionJson);
        submissionJson.setSubmission_id(submissionJsonId);

        // Save Object_feature data for each feature
        for (Object_feature feature : submissionJson.getFly_object_feature()) {
            feature.setSubmission_id(submissionJsonId);
            missionDao.addFeature(submissionJsonId, feature);
        }

        return submissionJson;
    }
}
