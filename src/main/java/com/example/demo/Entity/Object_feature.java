package com.example.demo.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Table(name = "object_feature")
public class Object_feature {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int object_feature_id;
    @Column(name="submission_id")
    private int submission_id;
    @Column(name = "name")
    private String name;
    @Column(name = "value")
    private String value;
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getValue() {
        return value;
    }
    public void setValue(String value) {
        this.value = value;
    }
    public int getObject_feature_id() {
        return object_feature_id;
    }
    public void setObject_feature_id(int object_feature_id) {
        this.object_feature_id = object_feature_id;
    }
    public int getSubmission_id() {
        return submission_id;
    }
    public void setSubmission_id(int submission_id) {
        this.submission_id = submission_id;
    }
    @Override
    public String toString() {
        return "Object_feature [object_feature_id=" + object_feature_id + ", submission_id=" + submission_id + ", name="
                + name + ", value=" + value + "]";
    }
    
    
}
