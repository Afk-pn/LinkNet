package com.linknet.backend.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "comments")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Integer id;

    @Column(nullable = false, length = 500)
    private String content;

    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    public Comment() {}

    public Comment(String content, Post post, User user) {
        this.content = content;
        this.post = post;
        this.user= user;
    }


    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

   public Post getPost() {
    return post;
}

public void setPost(Post post) {
    this.post = post;
}


public User getUser() {
    return user;
}

public void setUser(User user) {
    this.user = user;
}

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}

