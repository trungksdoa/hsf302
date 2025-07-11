package com.product.server.hsf_301.blindBox.controller;

import com.product.server.hsf_301.blindBox.model.*;
import com.product.server.hsf_301.blindBox.service.*;
import com.product.server.hsf_301.user.model.AppUser;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.security.core.Authentication;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    // Add these services
    private final SpinHistoryService spinHistoryService;
    private final UserPrizeItemService userPrizeItemService;
    private final OrderService orderService;
//    private final CollectionService collectionService;

    @GetMapping("/test")
    @ResponseBody
    public Page<SpinHistory> test (){
        AppUser user = userService.getUserById(2);
        if (user == null) {
            user = new AppUser(); // Fallback to empty user if not found
        }
        Page<SpinHistory> userSpinHistory = spinHistoryService.getSpinHistoryByUser(user,0, 10);

        return userSpinHistory;
    }

    @GetMapping("/profile")
    public String profile(Model model,
                          @RequestParam(defaultValue="0") int page,
                          @RequestParam(defaultValue="10") int size) {

        // For demonstration, we'll get a user by ID 2 (matching the seeded data)
        // In a real application, this would come from the authenticated user
        AppUser user = userService.getCurrentUser();
        if (user == null) {
            user = new AppUser(); // Fallback to empty user if not found
        }

        // Get user's spin history
//        Page<SpinHistory> userSpinHistory = spinHistoryService.getSpinHistoryByUser(user,page, size);
        Page<UserPrizeItem> userSpinHistory = userPrizeItemService.getByUserId(user.getUserId(),page,size);
        // Get user's order history (filter by COMPLETED status)
        List<Order> allOrders = orderService.getOrdersByUser(user);


        // Create Map for Order and OrderItems
        Map<Order, List<OrderItem>> orderItemMap = new HashMap<>();
        for (Order order : allOrders) {
            orderItemMap.put(order, order.getOrderItems() != null ? order.getOrderItems() : new ArrayList<>());
        }



        model.addAttribute("user", user);
        model.addAttribute("userSpinHistory", userSpinHistory);
        model.addAttribute("orderItemMap", orderItemMap); // Add this
        model.addAttribute("content", "view/profile");
        return "view/layout";
    }

    @GetMapping("/profile/{id}")
    public String profileById(@PathVariable Integer id, Model model) {
        AppUser user = userService.getUserById(id);
        if (user == null) {
            return "redirect:/error";
        }

        model.addAttribute("user", user);
        model.addAttribute("content", "user/profile");
        return "view/layout";
    }

    @PostMapping("/update")
    public String updateProfile(
            @ModelAttribute AppUser userUpdates,
            RedirectAttributes redirectAttributes) {

        try {
            // Update basic profile information
            userUpdates.setBalance(userService.getCurrentUser().getBalance());
            AppUser updatedUser = userService.updateProfile(userUpdates);
            redirectAttributes.addFlashAttribute("success", "Profile updated successfully");
            redirectAttributes.addFlashAttribute("user", updatedUser);

            return "redirect:/users/profile";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error updating profile: " + e.getMessage());
            return "redirect:/users/profile";
        }
    }

    @PostMapping("/change-password")
    public String changePasword(@RequestParam String oldPassword, @RequestParam String newPassword, @RequestParam String confirmPassword, RedirectAttributes redirectAttributes) {
        AppUser updatedUser = userService.getCurrentUser();

        if (newPassword != null && !newPassword.isEmpty()) {
            if (!userService.verifyPassword(updatedUser.getUserId(), oldPassword)) {
                redirectAttributes.addFlashAttribute("error", "Current password is incorrect");
                return "redirect:/users/profile";
            }

            if (!newPassword.equals(confirmPassword)) {
                redirectAttributes.addFlashAttribute("error", "New passwords don't match");
                return "redirect:/users/profile";
            }

            userService.updatePassword(updatedUser.getUserId(), newPassword);
            redirectAttributes.addFlashAttribute("success", "Password updated successfully");
        }

        return "redirect:/users/profile";
    }
    @GetMapping("/balance")
    @ResponseBody
    public String balance(Model model) {
        // Will replace with user id saved in Authorization
        AppUser user = userService.getCurrentUser();

        model.addAttribute("user", userService.getCurrentUser().getBalance());

        BigDecimal money = user.getBalance();


        String balance =  user.getBalance().toString();
        return balance;
    }
}
