package com.linknet.backend.service;

import com.linknet.backend.entity.Comment;
import com.linknet.backend.entity.Post;
import com.linknet.backend.entity.User;
import com.linknet.backend.repository.CommentRep;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService {

    @Autowired
    private CommentRep commentRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private PostService postService;


    public Comment createComment(Comment comment, Integer userId, Long postId) {

        User user = userService.findUserById(userId);
        Post post = postService.findPostById(postId);

        comment.setUser(user);
        comment.setPost(post);

        return commentRepository.save(comment);
    }


    public List<Comment> getAllComments() {
        return commentRepository.findAll();
    }


    public Comment findCommentById(Integer commentId) {

        return commentRepository.findById(commentId)
                .orElseThrow(() -> 
                    new RuntimeException("Comment not found with id: " + commentId)
                );
    }
    public void deleteComment(Integer commentId) {
    Comment comment = findCommentById(commentId);
    commentRepository.delete(comment);
}
public List<Comment> getCommentsByPost(Long postId) {
    return commentRepository.findByPostId(postId);
}

}