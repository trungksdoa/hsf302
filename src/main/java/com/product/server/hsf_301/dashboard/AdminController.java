package com.product.server.hsf_301.dashboard;


import com.product.server.hsf_301.blindBox.model.BlindPackage;
import com.product.server.hsf_301.blindBox.service.BlindBagTypeService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {
    private final BlindBagTypeService blindBagTypeService;
    private final String UPLOAD_DIR = "src/main/resources/static/uploads/blindbox/";

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
    @GetMapping("/blindBoxes/create")
    public String createBlindBox(Model model, HttpSession session) {
        model.addAttribute("showSidebar", true);
        model.addAttribute("blindPackage", new BlindPackage());
        return "admin/layout";
    }

    @GetMapping("/blogs")
    public String blog(Model model) {
        model.addAttribute("showSidebar", true);
        model.addAttribute("blogPosts", new ArrayList<>());
        model.addAttribute("content", "admin/blog/list");
        return "admin/layout";
    }
    @GetMapping("/update/{id}")
    public String updateBlindBox(@PathVariable Integer id, Model model) {
        model.addAttribute("showSidebar", true);
        BlindPackage blindPackage = blindBagTypeService.getBlindBagTypeById(id);
        model.addAttribute("blindPackage", blindPackage);
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

    //POST DATA

    @PostMapping("/blindBoxes")
    public String createBlindBox(@ModelAttribute BlindPackage blindPackage,
                                 @RequestParam("image") MultipartFile imageFile,
                                 RedirectAttributes redirectAttributes) {
        try {
            blindBagTypeService.saveBlindBagType(blindPackage, imageFile);
            redirectAttributes.addFlashAttribute("message", "blindPackage created successfully!");
            return "redirect:/admin/blindBoxes";
        } catch (IOException e) {
            redirectAttributes.addAttribute("error", "Failed to upload image: " + e.getMessage());
            return "redirect:/admin/blindBoxes";
        } catch (Exception e) {
            redirectAttributes.addAttribute("error", "Error creating blind box: " + e.getMessage());
            return "redirect:/admin/blindBoxes";
        }
    }

    @PostMapping("/update/{id}")
    public String updateBlindBox(@PathVariable Integer id,
                                 @ModelAttribute BlindPackage blindPackage,
                                 @RequestParam(value = "image", required = false) MultipartFile imageFile,
                                 RedirectAttributes redirectAttributes) {
        try {
            blindBagTypeService.updateBlindBagType(id, blindPackage, imageFile);
            redirectAttributes.addFlashAttribute("message", "Update successully!");

            return "redirect:/admin/blindBoxes";
        } catch (IOException e) {
            redirectAttributes.addFlashAttribute("error", "Failed to upload image: " + e.getMessage());
            return "redirect:/admin/blindBoxes";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error updating blind box: " + e.getMessage());
            return "redirect:/admin/blindBoxes";
        }
    }

    @GetMapping("/delete/{id}")
    public String deleteBlindBagType(@PathVariable Integer id) {
        blindBagTypeService.deleteBlindBagType(id);
        return "redirect:/blindBagTypes";
    }
}
