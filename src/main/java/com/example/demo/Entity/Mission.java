package com.example.demo.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

@Table(name = "mission")
public class Mission {
    @Column(name = "mission_id") // 任务id
    private String thread_id;
    @Column(name = "text")  // 任务描述
    private String text;
    @Column(name = "mission_type") //任务类型
    private String mission_type;
    @Column(name = "status")  //任务状态
    private String status;
    @Column(name = "deadline")  //任务截止时间
    private String deadline;
    @Column(name = "current_altitude") //当前位置经度
    private double current_longitude;
    @Column(name = "current_latitude") //当前位置纬度
    private double current_latitude;
    @Column(name = "target_longitude") //目标位置精度
    private double target_longitude;
    @Column(name = "target_latitude") //目标位置维度
    private double target_latitude;
    @Column(name = "length") //长
    private double length;
    @Column(name = "width") //宽
    private double width;

    @Transient
    private String imagePath;

    public String getThread_id() {
        return thread_id;
    }

    public void setThread_id(String thread_id) {
        this.thread_id = thread_id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getMission_type() {
        return mission_type;
    }

    public void setMission_type(String mission_type) {
        this.mission_type = mission_type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public double getCurrent_longitude() {
        return current_longitude;
    }

    public void setCurrent_longitude(double current_longitude) {
        this.current_longitude = current_longitude;
    }

    public double getCurrent_latitude() {
        return current_latitude;
    }

    public void setCurrent_latitude(double current_latitude) {
        this.current_latitude = current_latitude;
    }

    public double getTarget_longitude() {
        return target_longitude;
    }

    public void setTarget_longitude(double target_longitude) {
        this.target_longitude = target_longitude;
    }

    public double getTarget_latitude() {
        return target_latitude;
    }

    public void setTarget_latitude(double target_latitude) {
        this.target_latitude = target_latitude;
    }

    public double getLength() {
        return length;
    }

    public void setLength(double length) {
        this.length = length;
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    
    @Override
    public String toString() {
        return "Mission [thread_id=" + thread_id + ", text=" + text + ", mission_type=" + mission_type + ", status="
                + status + ", deadline=" + deadline + ", current_longitude=" + current_longitude + ", current_latitude="
                + current_latitude + ", target_longitude=" + target_longitude + ", target_latitude=" + target_latitude
                + ", length=" + length + ", width=" + width + "]";
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

}
