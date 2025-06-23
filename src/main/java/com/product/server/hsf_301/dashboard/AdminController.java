package com.product.server.hsf_301.dashboard;


import com.product.server.hsf_301.blindBox.model.*;
import com.product.server.hsf_301.blindBox.service.*;
import com.product.server.hsf_301.user.model.AppUser;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {
    private final BlindBagTypeService blindBagTypeService;
    private final BlogService blogService;
    private final OrderService orderService; // Add OrderService
    private final UserService userService;
    private final PrizeResetService resetService;
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

    @GetMapping("/update/{id}")
    public String updateBlindBox(@PathVariable Integer id, Model model) {
        model.addAttribute("showSidebar", true);
        PackagesBox blindPackage = blindBagTypeService.getBlindBagTypeById(id);
        model.addAttribute("blindPackage", blindPackage);
        return "admin/layout";
    }

    @GetMapping("/users")
    public String users(Model model, @RequestParam(value="page", defaultValue = "0") int page, @RequestParam(value="size", defaultValue = "10") int size ) {
        Page<AppUser> users = userService.getAllUsers(page, size); // Fetch actual orders
        model.addAttribute("showSidebar", true);
        model.addAttribute("users", users);
        model.addAttribute("content", "admin/users/list");
        return "admin/layout";
    }



    @GetMapping("/orders")
    public String orders(Model model) {
        List<Order> orders = orderService.getAllOrders(); // Fetch actual orders
        model.addAttribute("showSidebar", true);


        orders.forEach(order -> {
            if (order.getOrderItems() != null && !order.getOrderItems().isEmpty()) {
                double calculatedTotal = order.getOrderItems().stream()
                        .mapToDouble(OrderItem::getPrice)
                        .sum();
                order.setTotalAmount(calculatedTotal);
            }
        });

        double totalRevenue = orders.stream()
                .mapToDouble(Order::getTotalAmount)
                .sum();

        model.addAttribute("orders", orders);
        model.addAttribute("totalRevenue", totalRevenue);
        model.addAttribute("content", "admin/orders/list");
        return "admin/layout";
    }

    @GetMapping("/orders/updateStatus/{status}/{id}")
    public String updateStatus (@PathVariable("status") String status, @PathVariable("id") String id){
        orderService.updateOrderStatus(Integer.parseInt(id), status);
        return "redirect:/admin/orders"; // ← SỬA: Chỉ 1 dấu /
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

    @GetMapping("/users/{userId}")
    public String viewCustomerProfile(@PathVariable Integer userId, Model model) {
        AppUser user = userService.getUserById(userId);
        List<Order> userOrders = orderService.getOrdersByUser(user);

        // Calculate statistics
        double totalSpent = userOrders.stream()
                .mapToDouble(Order::getTotalAmount)
                .sum();

        long completedOrders = userOrders.stream()
                .filter(order -> "COMPLETED".equals(order.getStatus()))
                .count();

        model.addAttribute("showSidebar", true);
        model.addAttribute("user", user);
        model.addAttribute("userOrders", userOrders);
        model.addAttribute("totalSpent", totalSpent);
        model.addAttribute("completedOrders", completedOrders);
        model.addAttribute("content", "admin/users/profile");
        return "admin/layout";
    }


    // --- CRUD BlindBox ---
    @GetMapping("/blindBoxes/{id}")
    @ResponseBody
    public PackagesBox getBlindPack(@PathVariable("id") Integer id) {
        return blindBagTypeService.getBlindBagTypeById(id);
    }

    @PostMapping("/blindBoxes")
    public String createBlindBox(@ModelAttribute PackagesBox blindPackage,
                                 @RequestParam("imageFile") MultipartFile imageFile,
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
                                 @ModelAttribute PackagesBox blindPackage,
                                 @RequestParam(value = "image", required = false) MultipartFile imageFile,
                                 RedirectAttributes redirectAttributes) {
        try {
            blindPackage.setId(id);


            blindBagTypeService.saveBlindBagType(blindPackage, imageFile);
            redirectAttributes.addFlashAttribute("message", "blindPackage created successfully!");
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
        return "redirect:/admin/blindBoxes";
    }

    // --- CRUD Blogs ---

    @GetMapping("/blogs")
    public String blog(Model model, @RequestParam(value = "page",defaultValue = "0") int page,  @RequestParam(value="size",defaultValue = "10") int size) {
        Page<Blog> blogs = blogService.getAllBlogs(page , size);
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



    /**
     * Manual reset prizes endpoint
     */
    @PostMapping("/prizes/reset")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> resetPrizes() {
        Map<String, Object> response = new HashMap<>();

        try {
            int resetCount = resetService.manualResetPrizes();

            response.put("success", true);
            response.put("message", "Prizes reset successfully");
            response.put("resetCount", resetCount);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error resetting prizes: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }

    /**
     * Get prize statistics
     */
    @GetMapping("/prizes/statistics")
    @ResponseBody
    public ResponseEntity<PrizeResetService.PrizeStatistics> getPrizeStatistics() {
        try {
            PrizeResetService.PrizeStatistics stats = resetService.getPrizeStatistics();
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

}
