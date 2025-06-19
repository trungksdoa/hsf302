package com.product.server.hsf_301.blindBox.controller;

import com.product.server.hsf_301.blindBox.model.SpinHistory;
import com.product.server.hsf_301.user.model.User;
import com.product.server.hsf_301.blindBox.service.SpinHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/spinHistory")
public class SpinHistoryController {

    private final SpinHistoryService spinHistoryService;

    @Autowired
    public SpinHistoryController(SpinHistoryService spinHistoryService) {
        this.spinHistoryService = spinHistoryService;
    }

    @GetMapping
    public String getAllSpinHistory(Model model) {
        List<SpinHistory> spinHistories = spinHistoryService.getAllSpinHistory();
        model.addAttribute("spinHistories", spinHistories);
        model.addAttribute("showSidebar", false);
        model.addAttribute("content", "view/spin-history");
        return "view/layout";
    }
    
    @GetMapping("/{id}")
    public String getSpinHistoryById(@PathVariable Integer id, Model model) {
        SpinHistory spinHistory = spinHistoryService.getSpinHistoryById(id);
        model.addAttribute("spinHistory", spinHistory);
        return "spinHistory/details";
    }
    
    @GetMapping("/user/{userId}")
    public String getSpinHistoryByUser(@PathVariable Integer userId, Model model) {
        User user = new User(); // In a real app, you'd get this from a user service
        user.setUserId(userId);
        
        List<SpinHistory> spinHistories = spinHistoryService.getSpinHistoryByUser(user);
        model.addAttribute("spinHistories", spinHistories);
        model.addAttribute("userId", userId);
        return "spinHistory/userHistory";
    }
    
    @GetMapping("/redeem/{id}")
    public String redeemPrize(@PathVariable Integer id) {
        spinHistoryService.redeemPrize(id);
        return "redirect:/spinHistory";
    }
    
    @GetMapping("/spin/{userId}/{bagTypeId}")
    public String spin(@PathVariable Integer userId, @PathVariable Integer bagTypeId, Model model) {
        SpinHistory result = spinHistoryService.spin(userId, bagTypeId);
        model.addAttribute("spinResult", result);
        return "spinHistory/spinResult";
    }
}
