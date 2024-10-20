package com.example.demo.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;
import java.util.List;
import com.example.demo.Dao.MissionDao;
import com.example.demo.Entity.Mission;

@Service
public class CheckDeadlineService {
   @Autowired
    private MissionDao missionDao;

    // 定时任务，设置每隔1分钟执行一次
    @Scheduled(fixedRate = 60000) // 60秒
    public void checkDeadlines() {
        LocalDateTime now = LocalDateTime.now();
        
        // 获取所有正在执行的任务
        List<Mission> missions = missionDao.getActiveMissions();

        for (Mission mission : missions) {
            // 将 String 类型的 deadline 转换为 LocalDateTime
            LocalDateTime deadline = convertStringToLocalDateTime(mission.getDeadline());

            // 检查是否超时
            if (deadline != null && deadline.isBefore(now)) {
                // 标记任务为超时
                mission.setStatus("超时");
                missionDao.updatemission(mission); // 更新任务状态
                System.out.println("任务超时：" + mission.getThread_id());
            }
        }
    }

    // 将 String 转换为 LocalDateTime 的辅助方法
    private LocalDateTime convertStringToLocalDateTime(String deadline) {
        try {
            // 定义你使用的时间格式（例如 "yyyy-MM-dd HH:mm:ss"）
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            return LocalDateTime.parse(deadline, formatter);
        } catch (Exception e) {
            System.err.println("日期格式转换失败: " + deadline);
            return null; // 如果转换失败，返回 null
        }
    }
}
