package com.product.server.hsf_301.blindBox.controller;


import com.product.server.hsf_301.blindBox.model.User;
import com.product.server.hsf_301.blindBox.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @GetMapping("/profile")
    public String profile(Model model) {
        // For demonstration, we'll get a user by ID 1
        // In a real application, this would come from the authenticated user
        User user = userService.getUserById(1);
        if (user == null) {
            user = new User(); // Fallback to empty user if not found
        }



        model.addAttribute("user", user);
        model.addAttribute("content", "view/profile");
        return "view/layout";
    }
    
    @GetMapping("/profile/{id}")
    public String profileById(@PathVariable Integer id, Model model) {
        User user = userService.getUserById(id);
        if (user == null) {
            return "redirect:/error";
        }
        
        model.addAttribute("user", user);
        model.addAttribute("content", "user/profile");
        return "view/layout";
    }
    
    @PostMapping("/update")
    public String updateProfile(
            @ModelAttribute User userUpdates,
            @RequestParam(required = false) String currentPassword,
            @RequestParam(required = false) String newPassword,
            @RequestParam(required = false) String confirmPassword,
            RedirectAttributes redirectAttributes) {
        
        try {
            // Check if we're updating the password
            if (newPassword != null && !newPassword.isEmpty()) {
                if (!userService.verifyPassword(userUpdates.getUserId(), currentPassword)) {
                    redirectAttributes.addFlashAttribute("error", "Current password is incorrect");
                    return "redirect:/users/profile";
                }
                
                if (!newPassword.equals(confirmPassword)) {
                    redirectAttributes.addFlashAttribute("error", "New passwords don't match");
                    return "redirect:/users/profile";
                }
                
                userService.updatePassword(userUpdates.getUserId(), newPassword);
                redirectAttributes.addFlashAttribute("success", "Password updated successfully");
            }
            
            // Update basic profile information
            User updatedUser = userService.updateProfile(userUpdates);
            redirectAttributes.addFlashAttribute("success", "Profile updated successfully");
            redirectAttributes.addFlashAttribute("user", updatedUser);
            
            return "redirect:/users/profile";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error updating profile: " + e.getMessage());
            return "redirect:/users/profile";
        }
    }

    @GetMapping("/balance")
    @ResponseBody
    public String balance(Model model) {

        //Will replace with user id saved in Authorization
        User user = userService.getUserById(1);

        model.addAttribute("user", user);

        return user.getBalance().toString();
    }
}
