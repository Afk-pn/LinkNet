package com.linknet.backend.service;

import com.linknet.backend.entity.Post;
import com.linknet.backend.entity.User;
import com.linknet.backend.repository.PostRep;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostService {

    @Autowired
    private PostRep postRepository;

    @Autowired
    private UserService userService;

    public Post createPost(Integer user_id, String caption, String data){
        User u= userService.findUserById(user_id);

        Post post= new Post();
        post.setUser(u);
        post.setCaption(caption);
        
        if (isVideo(data)) {
        post.setVideo(data);
    } else {
        post.setImage(data);
    }

    return postRepository.save(post);
}

private boolean isVideo(String filePathOrUrl) {
    if (filePathOrUrl == null) return false;
    return filePathOrUrl.toLowerCase().matches(".*\\.(mp4|mov|avi|webm|mkv)$");
}


    public Post findPostById(Long id) {
        return postRepository.findById(id)
            .orElseThrow(() -> new IllegalStateException("Post not found with id: " + id));
    }

    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    public void deletePost(Long id) {
        Post post = findPostById(id);
        postRepository.delete(post);
    }

    public void editPost(Long id, String caption){
         Post existing= findPostById(id);
         if (caption != null) {
        existing.setCaption(caption);
    }
     postRepository.save(existing);
    }

}