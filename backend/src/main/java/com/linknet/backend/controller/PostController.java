package com.linknet.backend.controller;

import com.linknet.backend.entity.Post;
import com.linknet.backend.repository.CommentRep;
import com.linknet.backend.service.PostService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;


import java.nio.file.Path;


@RestController
@RequestMapping("/api/posts")
public class PostController {

    @Autowired
    private PostService postService;

    @Autowired
    private CommentRep commentRepository;

    @PostMapping
    public Post createPost(@RequestParam Integer userId,
                            @RequestParam String caption,
                            @RequestParam String data) {
        return postService.createPost(userId, caption, data);
    }

    @GetMapping
    public List<Post> getAllPosts() {
        return postService.getAllPosts();
    }

    @GetMapping("/{id}")
    public Post getPostById(@PathVariable Long id) {
        return postService.findPostById(id);
    }

    @PutMapping("/{id}")
    public void editPost(@PathVariable Long id, @RequestParam String caption) {
        postService.editPost(id, caption);
    }

    @DeleteMapping("/{id}")
    public void deletePost(@PathVariable Long id) {
        commentRepository.deleteAll(commentRepository.findByPostId(id));
        postService.deletePost(id);
    }

    @PostMapping("/upload")
public String uploadFile(@RequestParam("file") MultipartFile file) throws IOException {

    Path uploadDir = Paths.get("uploads").toAbsolutePath().normalize();
    Files.createDirectories(uploadDir); // ensure the folder exists

    String filename = System.currentTimeMillis() + "_" + file.getOriginalFilename();
    Path filePath = uploadDir.resolve(filename);

    file.transferTo(filePath); // note: pass a Path, not filePath.toFile()

    return "http://localhost:8080/uploads/" + filename;
}
}