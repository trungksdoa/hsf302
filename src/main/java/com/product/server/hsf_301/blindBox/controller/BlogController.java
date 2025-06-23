package com.product.server.hsf_301.blindBox.controller;


import com.product.server.hsf_301.blindBox.model.Blog;
import com.product.server.hsf_301.blindBox.service.BlogService;
import com.product.server.hsf_301.blindBox.service.UserService;
import com.product.server.hsf_301.user.model.AppUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
@RequiredArgsConstructor
@RequestMapping("/blogs")
public class BlogController {
    private final BlogService blogService;
    private final UserService userService;

    @GetMapping
    public String blog(Model model, 
                       @RequestParam(required = false) String author,
                       @RequestParam(defaultValue = "0") int page, 
                       @RequestParam(defaultValue = "10") int size) {
        model.addAttribute("posts", blogService.getAllBlogs(page, size));

        // Get current user's posts (using hardcoded "2" for now, replace with actual user ID)

        model.addAttribute("userPosts", blogService.getBlogsByAuthor(userService.getCurrentUser()));
        model.addAttribute("totalPosts", blogService.getTotalPostCount());
//        model.addAttribute("totalUserPost", blogService.getPostCountByAuthor(userService.getCurrentUser()));
//        // If filtering by current user
//        if ("current".equals(author)) {
//            model.addAttribute("posts", blogService.getBlogsByAuthor(currentUserId));
//        }

        model.addAttribute("content", "view/blog/list");
        return "view/layout";
    }

    @GetMapping("/my-posts")
    public String myPosts(Model model,
                          @RequestParam(defaultValue = "0") int page,
                          @RequestParam(defaultValue = "10") int size) {
        AppUser currentUser = userService.getCurrentUser();
        List<Blog> userPosts = blogService.getBlogsByAuthor(currentUser);
        Long totalUserPosts = blogService.getPostCountByAuthor(currentUser);

        model.addAttribute("posts", userPosts);
        model.addAttribute("currentUser", currentUser);
        model.addAttribute("totalUserPosts", totalUserPosts);
        model.addAttribute("page", page);
        model.addAttribute("size", size);
        model.addAttribute("content", "view/blog/my-posts");
        return "view/layout";
    }


    @PostMapping
    public String postBlog(@ModelAttribute Blog blog) {
        AppUser user = new AppUser();
        user.setUserId(2);
        blog.setAuthor(user);
        blogService.createBlog(blog);
        return "redirect:/blogs";
    }

    @GetMapping("/{id}/edit")
    public String editBlogPage(@PathVariable("id") Long id, Model model) {
        Optional<Blog> blogOptional = blogService.getBlogById(id);
        if (blogOptional.isPresent()) {
            model.addAttribute("blog", blogOptional.get());
            model.addAttribute("content", "view/blog/edit");
            return "view/layout";
        } else {
            return "redirect:/blogs";
        }
    }

    @PostMapping("/{id}/edit")
    public String updateBlog(@PathVariable("id") Long id, @ModelAttribute Blog blog) {
        blog.setId(id);
        AppUser user = new AppUser();
        user.setUserId(2);
        blog.setAuthor(user);
        blogService.updateBlog(id, blog);
        return "redirect:/blogs";
    }

    @DeleteMapping("/{id}")
    public String deleteBlog(@PathVariable("id") Long id) {
        blogService.deleteBlog(id);
        return "redirect:/blogs";
    }

    @GetMapping("/{id}")
    public String blogDetail(@PathVariable("id") Long id, Model model) {
        Optional<Blog> blogOptional = blogService.getBlogById(id);
        if (blogOptional.isPresent()) {
            model.addAttribute("blog", blogOptional.get());

            model.addAttribute("content", "view/blog/detail");
            return "view/layout";
        } else {
            return "redirect:/blogs";
        }
    }
}
