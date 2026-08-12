package com.linknet.backend.repository;

import com.linknet.backend.entity.Comment;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRep extends JpaRepository<Comment, Integer> {
   List<Comment> findByPostId(Long postId);
}