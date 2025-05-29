package com.product.server.hsf_301.dashboard;


import com.product.server.hsf_301.blindBox.service.BlindBagTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {
    private final BlindBagTypeService blindBagTypeService;

    @GetMapping
    public String index(Model model) {
        model.addAttribute("showSidebar", true);
        model.addAttribute("content", "admin/dashboard");
        return "admin/layout";
    }

    @GetMapping("/blindBoxes")
    public String blindBox(Model model, @RequestParam(defaultValue = "0" , required = false) Integer page, @RequestParam(defaultValue = "10", required = false) Integer size) {
        model.addAttribute("showSidebar", true);
        model.addAttribute("blindList", blindBagTypeService.getAllBlindBagTypes(page,size));
        model.addAttribute("content", "admin/blindbox/list");
        return "admin/layout";
    }

    @GetMapping("/blogs")
    public String blog(Model model) {
        model.addAttribute("showSidebar", true);
        model.addAttribute("blogPosts", new ArrayList<>());
        model.addAttribute("content", "admin/blog/list");
        return "admin/layout";
    }

    @GetMapping("/orders")
    public String orders(Model model) {
        model.addAttribute("showSidebar", true);
        model.addAttribute("orders", new ArrayList<>());
        model.addAttribute("content", "admin/orders/list");
        return "admin/layout";
    }

    @GetMapping("/users")
    public String users(Model model) {
        model.addAttribute("showSidebar", true);
        model.addAttribute("users", new ArrayList<>());
        model.addAttribute("content", "admin/users/list");
        return "admin/layout";
    }
}
