package com.product.server.hsf_301.dashboard;


import com.product.server.hsf_301.blindBox.model.BlindPackage;
import com.product.server.hsf_301.blindBox.service.BlindBagTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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

    //POST DATA

    @PostMapping("/blindBoxes")
    public String createBlindBox(@ModelAttribute BlindPackage blindPackage,
                                 @RequestParam("imageFile") MultipartFile imageFile,
                                 Model model) {
        try {
            // Handle file upload if a file was provided
            if (imageFile != null && !imageFile.isEmpty()) {
                // Create directory if it doesn't exist
                Path uploadPath = Paths.get(UPLOAD_DIR);
                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }

                // Generate a unique filename to prevent overwriting
                String originalFilename = imageFile.getOriginalFilename();
                String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
                String newFilename = UUID.randomUUID().toString() + fileExtension;

                // Save the file
                Path filePath = uploadPath.resolve(newFilename);
                Files.copy(imageFile.getInputStream(), filePath);

                // Set the image URL to the uploaded file path
                blindPackage.setImageUrl("/uploads/blindbox/" + newFilename);
            }

            // Save the blind package
            blindBagTypeService.saveBlindBagType(blindPackage);

            model.addAttribute("success", "Blind box created successfully");
            // Refresh blind box list
            Page<BlindPackage> blindBagTypes = blindBagTypeService.getAllBlindBagTypes(0, 10);
            model.addAttribute("blindList", blindBagTypes);

            return "redirect:/admin/blindBoxes";
        } catch (IOException e) {
            model.addAttribute("error", "Failed to upload image: " + e.getMessage());
            return "redirect:/admin/blindBoxes";
        } catch (Exception e) {
            model.addAttribute("error", "Error creating blind box: " + e.getMessage());
            return "redirect:/admin/blindBoxes";
        }
    }

    @PostMapping("/update/{id}")
    public String updateBlindBox(@PathVariable Integer id,
                                 @ModelAttribute BlindPackage blindPackage,
                                 @RequestParam(value = "imageFile", required = false) MultipartFile imageFile,
                                 Model model) {
        try {
            // Make sure the ID is set correctly
            blindPackage.setId(id);

            // Handle file upload if a file was provided
            if (imageFile != null && !imageFile.isEmpty()) {
                // Create directory if it doesn't exist
                Path uploadPath = Paths.get(UPLOAD_DIR);
                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }

                // Generate a unique filename to prevent overwriting
                String originalFilename = imageFile.getOriginalFilename();
                String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
                String newFilename = UUID.randomUUID().toString() + fileExtension;

                // Save the file
                Path filePath = uploadPath.resolve(newFilename);
                Files.copy(imageFile.getInputStream(), filePath);

                // Set the image URL to the uploaded file path
                blindPackage.setImageUrl("/uploads/blindbox/" + newFilename);
            } else if (blindPackage.getImageUrl() == null || blindPackage.getImageUrl().isEmpty()) {
                // If no new file and no existing URL, get the existing image URL from the database
                BlindPackage existingBox = blindBagTypeService.getBlindBagTypeById(id);
                blindPackage.setImageUrl(existingBox.getImageUrl());
            }

            // Save the blind package
            blindBagTypeService.saveBlindBagType(blindPackage);

            model.addAttribute("success", "Blind box updated successfully");
            // Refresh blind box list
            Page<BlindPackage> blindBagTypes = blindBagTypeService.getAllBlindBagTypes(0, 10);
            model.addAttribute("blindList", blindBagTypes);

            return "redirect:/admin/blindBoxes";
        } catch (IOException e) {
            model.addAttribute("error", "Failed to upload image: " + e.getMessage());
            return "redirect:/admin/blindBoxes";
        } catch (Exception e) {
            model.addAttribute("error", "Error updating blind box: " + e.getMessage());
            return "redirect:/admin/blindBoxes";
        }
    }

    @GetMapping("/delete/{id}")
    public String deleteBlindBagType(@PathVariable Integer id) {
        blindBagTypeService.deleteBlindBagType(id);
        return "redirect:/blindBagTypes";
    }
}
