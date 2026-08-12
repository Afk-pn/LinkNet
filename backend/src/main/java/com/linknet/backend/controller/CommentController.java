package com.linknet.backend.controller;

import com.linknet.backend.entity.Comment;
import com.linknet.backend.service.CommentService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @PostMapping
    public Comment createComment(@RequestParam Integer userId,
                                  @RequestParam Long postId,
                                  @RequestBody Comment comment) {
        return commentService.createComment(comment, userId, postId);
    }

    @GetMapping
    public List<Comment> getAllComments() {
        return commentService.getAllComments();
    }

    @GetMapping("/{id}")
    public Comment getCommentById(@PathVariable Integer id) {
        return commentService.findCommentById(id);
    }

    @GetMapping("/post/{postId}")
    public List<Comment> getCommentsByPost(@PathVariable Long postId) {
        return commentService.getCommentsByPost(postId);
    }

    @DeleteMapping("/{id}")
    public void deleteComment(@PathVariable Integer id) {
        commentService.deleteComment(id);
    }
}