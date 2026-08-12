package com.linknet.backend.repository;

import com.linknet.backend.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRep extends JpaRepository<Post, Long> {
   
}
