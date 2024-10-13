package com.example.demo.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Table;

@Table(name="mission")
public class Mission {
    @Column(name="thread_id")
    private String thread_id;  // 修改字段名为 thread_id
    @Column(name="text")
    private String text;

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

    @Override
    public String toString() {
        return "Mission [thread_id=" + thread_id + ", text=" + text + "]";
    }
    
}
