package com.example.demo.Entity;


public class Returntype {
    private String thread_id;
    private String text;
    private String fly_type;
    private String fly_object;
    private int submission_id;
    private String name;
    private String value;
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
    public int getSubmission_id() {
        return submission_id;
    }
    public void setSubmission_id(int submission_id) {
        this.submission_id = submission_id;
    }

    @Override
    public String toString() {
        return "Returntype [fly_object=" + fly_object + ", fly_type=" + fly_type + ", name=" + name + ", submission_id="
                + submission_id + ", text=" + text + ", thread_id=" + thread_id + ", value=" + value + "]";
    }
    
}
