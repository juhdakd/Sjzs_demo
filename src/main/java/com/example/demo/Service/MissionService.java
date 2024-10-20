package com.example.demo.Service;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.Dao.MissionDao;
import com.example.demo.Entity.Mission;
import com.example.demo.Entity.Object_feature;
import com.example.demo.Entity.Returntype;
import com.example.demo.Entity.Submission;
import com.example.demo.Interface.LLMInterface;
import com.example.demo.Interface.outputtojson;

import jakarta.annotation.Resource;

@Service
public class MissionService {

    @Resource
    private MissionDao missionDao;

    @Autowired
    private LLMInterface llmInterface;

    public Object input(Mission mission) {
        // 使用时间生成一个特定的编号 时间戳
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyMMddHHmmss");
        String thread_id = now.format(formatter);
        mission.setThread_id(thread_id);
        missionDao.input(mission);
        mission.setStatus("未执行");

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

    public List<Submission> getAll() {
        // 从数据库获取所有 mission 数据
        List<Returntype> listReturntypes = missionDao.getAll();
        System.out.println("listReturntypes: " + listReturntypes.toString());

        // 用来存储所有的 Submission 对象
        Map<String, Submission> submissionMap = new HashMap<>();

        // 遍历所有的 Returntype 对象
        for (Returntype returntype : listReturntypes) {
            String threadId = returntype.getThread_id();

            // 如果 submissionMap 中还没有该 thread_id 的 Submission，则创建并添加
            if (!submissionMap.containsKey(threadId)) {
                Submission submission = new Submission();
                submission.setSubmission_id(returntype.getSubmission_id());
                submission.setThread_id(returntype.getThread_id());
                submission.setFly_type(returntype.getFly_type());
                submission.setFly_object(returntype.getFly_object());

                // 添加到 map 中
                submissionMap.put(threadId, submission);
            }

            // 获取当前的 Submission
            Submission currentSubmission = submissionMap.get(threadId);

            // 为当前 Submission 创建 Object_feature 对象，并添加到 fly_object_feature 列表中
            Object_feature objectFeature = new Object_feature();
            objectFeature.setName(returntype.getName());
            objectFeature.setValue(returntype.getValue());

            // 如果当前 Submission 的 feature 列表为空，则初始化列表
            if (currentSubmission.getFly_object_feature() == null) {
                currentSubmission.setFly_object_feature(new ArrayList<>());
            }

            // 添加 Object_feature 对象到 Submission 的 fly_object_feature 列表
            currentSubmission.getFly_object_feature().add(objectFeature);
        }

        // 将 map 中所有的 Submission 转换为 List 并返回
        return new ArrayList<>(submissionMap.values());
    }

    // 更改=重新输入信息，update对应的mission 并删除对应的submission 添加新的submission
    public Submission update(Mission mission) {
        // 处理新的输入信息
        // 删除对应的submission
        missionDao.deleteSubmission(mission);
        // 更新mission的text
        missionDao.updatemission(mission);
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

    public Submission inputMissionWithImage(Mission mission, MultipartFile imageFile) throws IOException {
        // 使用时间生成一个特定的编号 时间戳
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyMMddHHmmss");
        String thread_id = now.format(formatter);
        mission.setThread_id(thread_id);
        missionDao.input(mission);

        System.out.println("Mission: " + mission.toString());

        String imagePath = null;
        // 处理图片文件上传
        if (imageFile != null && !imageFile.isEmpty()) {
            // 保存图片文件并返回文件路径
            imagePath = saveImage(imageFile, mission.getThread_id());
            System.out.println("Image Path: " + imagePath);
        }
        System.out.println("开始发送请求");
        // 发送 POST 请求到外部接口，附带图片
        Submission submissionJson = llmInterface.sendPostRequestWithImage(mission, imagePath);

        outputtojson outjson = new outputtojson();
        // 将 Submission 数据插入数据库
        missionDao.addSubmission(submissionJson);
        int submissionJsonId = missionDao.getsubmissionid(submissionJson);
        // 为啥是0呢？
        submissionJson.setSubmission_id(submissionJsonId);
        // 将 Object_feature 数据插入数据库
        for (Object_feature feature : submissionJson.getFly_object_feature()) {
            feature.setSubmission_id(submissionJsonId);
            missionDao.addFeature(submissionJsonId, feature);
        }

        System.out.println("返回的Submission: " + outjson.formatSubmissionToJson(submissionJson));
        return submissionJson;
    }

    // 保存图片到文件系统或数据库
    private String saveImage(MultipartFile imageFile, String thread_id) throws IOException {
        String folder = "E:\\Learnn\\数聚智算平台\\demo\\src\\main\\resources\\static\\image\\";
        String fileName = thread_id + ".jpg";
        String filePath = folder + fileName;

        File dest = new File(filePath);
        imageFile.transferTo(dest);

        return filePath;
    }
}
