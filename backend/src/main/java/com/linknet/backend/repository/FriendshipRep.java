package com.linknet.backend.repository;

import com.linknet.backend.entity.Friendship;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FriendshipRep extends JpaRepository<Friendship, Integer> {
}
