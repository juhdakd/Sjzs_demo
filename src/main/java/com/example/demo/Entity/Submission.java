package com.example.demo.Entity;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

@Table(name = "submission")
public class Submission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int submission_id;
    @Column(name = "fly_type")
    private String fly_type;
    @Column(name = "fly_object")
    private String fly_object;
    @Column(name = "thread_id")
    private String thread_id;
    
    @Transient
    private List<Object_feature> fly_object_feature;

    
    public String getFly_type() {
        return fly_type;
    }

    public void setFly_type(String fly_type) {
        this.fly_type = fly_type;
    }

    public String getFly_object() {
        return fly_object;
    }

    public void setFly_object(String fly_object) {
        this.fly_object = fly_object;
    }

    public int getSubmission_id() {
        return submission_id;
    }

    public void setSubmission_id(int submission_id) {
        this.submission_id = submission_id;
    }

    public String getThread_id() {
        return thread_id;
    }

    public void setThread_id(String thread_id) {
        this.thread_id = thread_id;
    }

    public List<Object_feature> getFly_object_feature() {
        return fly_object_feature;
    }

    public void setFly_object_feature(List<Object_feature> fly_object_feature) {
        this.fly_object_feature = fly_object_feature;
    }

    @Override
    public String toString() {
        return "Submission [submission_id=" + submission_id + ", fly_type=" + fly_type + ", fly_object=" + fly_object
                + ", thread_id=" + thread_id + ", fly_object_feature=" + fly_object_feature + "]";
    }

    
}
