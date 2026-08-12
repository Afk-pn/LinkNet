package com.linknet.backend.dto;

public class PostRequest {
    private Long userId;
    private String content;

    //im sending only id and content
    public Long getUserId() { 
        return userId; }
    public void setUserId(Long userId) { 
        this.userId = userId; }
    public String getContent() {
         return content; }
    public void setContent(String content) { 
        this.content = content; }
}
