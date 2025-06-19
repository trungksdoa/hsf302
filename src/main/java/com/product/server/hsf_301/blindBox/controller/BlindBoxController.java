package com.product.server.hsf_301.blindBox.controller;

import com.product.server.hsf_301.blindBox.model.PackagesBox;
import com.product.server.hsf_301.blindBox.model.PrizeItem;
import com.product.server.hsf_301.blindBox.model.SpinHistory;
import com.product.server.hsf_301.blindBox.service.*;

import com.product.server.hsf_301.user.model.AppUser;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/blindbox")
@RequiredArgsConstructor
public class BlindBoxController {

    private final BlindBagTypeService blindBagTypeService;
    private final PrizeItemService prizeItemService;
//    private final SpinHistoryService spinHistoryService;
    private final UserService userService;
    private final SpinService spin;
    


    @GetMapping("/{id}")
    public String detail(@PathVariable Integer id, Model model) {
        PackagesBox blindBagType = blindBagTypeService.getBlindBagTypeById(id);
        model.addAttribute("blindBagType", blindBagType);
        return "blindBagType/details";
    }




    @PostMapping("/{boxId}/spin")
    @ResponseBody
    public ResponseEntity<?> spinBox(@PathVariable Integer boxId) {
        try {
            // This is for future implementation - right now we're just returning a stub response
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("spinId", 1);
            response.put("itemId", 1);
            response.put("itemName", "Sample Prize");
            response.put("itemImage", "https://via.placeholder.com/200x200?text=Prize");
            response.put("rarity", "RARE");
            response.put("timestamp", java.time.LocalDateTime.now());
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", "Error: " + e.getMessage()
            ));
        }
    }
    @GetMapping("/packages")
    public String showPackages(Model model) {
        List<PackagesBox> activePackages = blindBagTypeService.getAllBlindBagTypes();
        model.addAttribute("packages", activePackages);
        return "blind-box/packages";
    }
    @GetMapping("/spin/{id}")
    @ResponseBody
    public ResponseEntity<?> getSpinPageData(@PathVariable Integer id) {
        try {
            PackagesBox blindPackage = blindBagTypeService.getBlindBagTypeById(id);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("blindPackage", blindPackage);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", "Error: " + e.getMessage()
            ));
        }
    }

    @PostMapping("/spin/{id}/single")
    @ResponseBody
    public ResponseEntity<?> spinOnce(@PathVariable Integer id) {
        try {
            PackagesBox blindPackage = blindBagTypeService.getBlindBagTypeById(id);
            AppUser user = userService.getCurrentUser();

            SpinHistory result = spin.spinItem(user, blindPackage);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", result.getSuccess());
            
            if (result.getSuccess() && result.getPrizeItemId() != null) {
                // Won an actual prize
                PrizeItem prizeItem = result.getPrizeItemId();
                response.put("spinId", result.getHistoryId());
                response.put("itemId", prizeItem.getItemId());
                response.put("itemName", prizeItem.getItemName());
                response.put("itemImage", prizeItem.getImageUrl());
                response.put("rarity", prizeItem.getRarity());
                response.put("description", "Congratulations on your prize!");
                response.put("timestamp", result.getSpinTime());
            } else {
                // GOOD_LUCK or error case
                response.put("message", "Better luck next time!");
            }
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", "Error: " + e.getMessage()
            ));
        }
    }
    @PostMapping("/spin/{id}/multiple")
    public ResponseEntity<?> spinFiveTimes(@PathVariable Integer id) {
        try {
            PackagesBox blindPackage = blindBagTypeService.getBlindBagTypeById(id);
            AppUser user = userService.getCurrentUser();

            List<SpinHistory> results = spin.spinItems(user, blindPackage, 5);
//            redirectAttributes.addFlashAttribute("spinResults", results);
            Map<String, Object> response = new HashMap<>();
            response.put("success", "OK nhớ");
            response.put("items", results);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", "Error: " + e.getMessage()
            ));
        }

    }
    private void addSpinResultMessage(RedirectAttributes redirectAttributes, SpinHistory result) {
        if (result.getSuccess()) {
            redirectAttributes.addFlashAttribute("success",
                    "Congratulations! You won: " + result.getPrizeItemId().getItemName());
        } else {
            redirectAttributes.addFlashAttribute("error",
                    "Spin failed: " + "false");
        }
    }

}
