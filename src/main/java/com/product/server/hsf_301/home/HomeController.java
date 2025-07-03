package com.product.server.hsf_301.home;


import com.product.server.hsf_301.blindBox.model.PackagesBox;
import com.product.server.hsf_301.blindBox.service.BlindBagTypeService;
import com.product.server.hsf_301.blindBox.service.BlogService;
import com.product.server.hsf_301.blindBox.service.PrizeItemService;
import com.product.server.hsf_301.blindBox.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("/")
public class HomeController {
    private final BlindBagTypeService blindBagTypeService;
    private final BlogService blogService;
    private final PrizeItemService prizeItemService;
    private final UserService userService;
    @GetMapping
    public String home(Model model, HttpServletRequest request, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        Map<String, String> contactInfor = new HashMap<>();
        contactInfor.put("phone","0335857132");
        contactInfor.put("email","fpt@fpt.com");
        contactInfor.put("address","FPT");
        model.addAttribute("showSidebar", false);
        model.addAttribute("contactInfor", contactInfor);
        model.addAttribute("title", "Home");

        // Add null check for request
        if (request != null) {
            model.addAttribute("httpServletRequest", request);
        }

        Page<PackagesBox> blindBagTypes = blindBagTypeService.getAllBlindBagTypes(page, size);

        model.addAttribute("userId", userService.getCurrentUser().getUserId());
        model.addAttribute("products", blindBagTypes);
        model.addAttribute("recentPosts",blogService.getAllBlogs(0, 5));
        model.addAttribute("content", "view/home");
        return "view/layout";
    }
}
