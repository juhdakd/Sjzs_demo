package com.example.demo.Entity;

public class LLM_Response {
    public String thread_id;
    public String text;
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
        return "LLM_Response [thread_id=" + thread_id + ", text=" + text + "]";
    }

    
}
