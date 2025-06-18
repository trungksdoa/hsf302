package com.product.server.hsf_301.dashboard;


import com.product.server.hsf_301.blindBox.model.BlindPackage;
import com.product.server.hsf_301.blindBox.model.Blog;
import com.product.server.hsf_301.blindBox.model.Order;
import com.product.server.hsf_301.blindBox.service.BlindBagTypeService;
import com.product.server.hsf_301.blindBox.service.BlogService;
import com.product.server.hsf_301.blindBox.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.hibernate.Hibernate.size;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {
    private final BlindBagTypeService blindBagTypeService;
    private final BlogService blogService;
    private final OrderService orderService; // Add OrderService
    private final String UPLOAD_DIR = "src/main/resources/static/uploads/blindbox/";

    // Trang Dashboard
    @GetMapping
    public String index(Model model) {
        model.addAttribute("showSidebar", true);
        model.addAttribute("content", "admin/dashboard");
        return "admin/layout";
    }

    // Trang quản lý Blind Box
    @GetMapping("/blindBoxes")
    public String blindBox(Model model,
                           @RequestParam(defaultValue = "0") Integer page,
                           @RequestParam(defaultValue = "10") Integer size) {
        model.addAttribute("showSidebar", true);
        model.addAttribute("blindList", blindBagTypeService.getAllBlindBagTypes(page, size));
        model.addAttribute("content", "admin/blindbox/list");
        return "admin/layout";
    }

    // Trang quản lý blog


    @GetMapping("/orders")
    public String orders(Model model) {
        List<Order> orders = orderService.getAllOrders(); // Fetch actual orders
        model.addAttribute("showSidebar", true);
        model.addAttribute("orders", orders);
        model.addAttribute("content", "admin/orders/list");
        return "admin/layout";
    }

    // Add order status update endpoint
    @PostMapping("/orders/{orderId}/status")
    @ResponseBody
    public ResponseEntity<?> updateOrderStatus(@PathVariable Integer orderId,
                                              @RequestBody Map<String, String> request) {
        try {
            String status = request.get("status");
            orderService.updateOrderStatus(orderId, status);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // Add view order details endpoint
    @GetMapping("/orders/{orderId}")
    public String viewOrderDetails(@PathVariable Integer orderId, Model model) {
        Order order = orderService.getOrderById(orderId);
        model.addAttribute("showSidebar", true);
        model.addAttribute("order", order);
        model.addAttribute("content", "admin/orders/details");
        return "admin/layout";
    }

    // --- CRUD BlindBox ---
    @PostMapping("/blindBoxes")
    public String createBlindBox(@ModelAttribute BlindPackage blindPackage,
                                 @RequestParam("imageFile") MultipartFile imageFile,
                                 Model model) {
        try {
            if (imageFile != null && !imageFile.isEmpty()) {
                Path uploadPath = Paths.get(UPLOAD_DIR);
                if (!Files.exists(uploadPath)) Files.createDirectories(uploadPath);

                String originalFilename = imageFile.getOriginalFilename();
                String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
                String newFilename = UUID.randomUUID() + fileExtension;
                Files.copy(imageFile.getInputStream(), uploadPath.resolve(newFilename));
                blindPackage.setImageUrl("/uploads/blindbox/" + newFilename);
            }

            blindBagTypeService.saveBlindBagType(blindPackage);
            return "redirect:/admin/blindBoxes";
        } catch (Exception e) {
            model.addAttribute("error", "Error: " + e.getMessage());
            return "redirect:/admin/blindBoxes";
        }
    }

    @PostMapping("/update/{id}")
    public String updateBlindBox(@PathVariable Integer id,
                                 @ModelAttribute BlindPackage blindPackage,
                                 @RequestParam(value = "imageFile", required = false) MultipartFile imageFile,
                                 Model model) {
        try {
            blindPackage.setId(id);
            if (imageFile != null && !imageFile.isEmpty()) {
                Path uploadPath = Paths.get(UPLOAD_DIR);
                if (!Files.exists(uploadPath)) Files.createDirectories(uploadPath);

                String fileExtension = imageFile.getOriginalFilename().substring(imageFile.getOriginalFilename().lastIndexOf("."));
                String newFilename = UUID.randomUUID() + fileExtension;
                Files.copy(imageFile.getInputStream(), uploadPath.resolve(newFilename));
                blindPackage.setImageUrl("/uploads/blindbox/" + newFilename);
            } else {
                BlindPackage existingBox = blindBagTypeService.getBlindBagTypeById(id);
                blindPackage.setImageUrl(existingBox.getImageUrl());
            }

            blindBagTypeService.saveBlindBagType(blindPackage);
            return "redirect:/admin/blindBoxes";
        } catch (Exception e) {
            model.addAttribute("error", "Error: " + e.getMessage());
            return "redirect:/admin/blindBoxes";
        }
    }

    @GetMapping("/delete/{id}")
    public String deleteBlindBagType(@PathVariable Integer id) {
        blindBagTypeService.deleteBlindBagType(id);
        return "redirect:/admin/blindBoxes";
    }

    // --- CRUD Blogs ---

    @GetMapping("/blogs")
    public String blog(Model model) {
        List<Blog> blogs = blogService.getAllBlogs();
        System.out.println(size(blogService.getAllBlogs()));
        model.addAttribute("blogs", blogs); // Đảm bảo đúng tên biến
        model.addAttribute("content", "admin/blog/list"); // Không có .html ở đây
        return "admin/layout";
    }

    @PostMapping("/blog/create")
    public String createBlog(@ModelAttribute Blog blog) {
        blogService.createBlog(blog);
        return "redirect:/admin/blogs";
    }

    @PostMapping("/blog/update/{id}")
    public String updateBlog(@PathVariable Long id, @ModelAttribute Blog blog) {
        blogService.updateBlog(id, blog);
        return "redirect:/admin/blogs";
    }
    @DeleteMapping("/blog/delete/{id}")
    public ResponseEntity<Void> deleteBlog(@PathVariable Long id) {
        blogService.deleteBlog(id);
        return ResponseEntity.ok().build();
    }
//    @PostMapping("/blog/delete/{id}")
//    public String deleteBlog(@PathVariable Long id) {
//        System.out.println(blogService.getBlogById(id));
//        blogService.deleteBlog(id);
//        return "redirect:/admin/blogs";
//    }
}
