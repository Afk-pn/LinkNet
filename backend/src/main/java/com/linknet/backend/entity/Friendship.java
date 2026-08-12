package com.linknet.backend.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "friendships")
public class Friendship {

/**
 * The MySQL 'friendships' table has real foreign keys with ON DELETE CASCADE
 * on both user columns. The properly-matching JPA version would look like this:
 *
 * @ManyToOne
 * @JoinColumn(name = "user_id_1", nullable = false)
 * private User user1;
 *
 * @ManyToOne
 * @JoinColumn(name = "user_id_2", nullable = false)
 * private User user2;
 */

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "friendship_id")
    private Integer friendshipId;

    @Column(name = "user_id_1", nullable = false)
    private Integer userId1;

    @Column(name = "user_id_2", nullable = false)
    private Integer userId2;

    public Friendship() {}

    public Friendship(Integer userId1, Integer userId2) {
        this.userId1 = userId1;
        this.userId2 = userId2;
    }

    public Integer getFriendshipId() {
         return friendshipId; 
        }

    public void setFriendshipId(Integer friendshipId) { 
        this.friendshipId = friendshipId; 
    }

    public Integer getUserId1() { 
        return userId1;
    }
    public void setUserId1(Integer userId1) { 
        this.userId1 = userId1; 
    }

    public Integer getUserId2() { 
        return userId2; 
    }
    public void setUserId2(Integer userId2) { 
        this.userId2 = userId2; 
    }
}
