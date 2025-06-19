package com.product.server.hsf_301.blindBox.controller;

import com.product.server.hsf_301.blindBox.model.BlindPackage;
import com.product.server.hsf_301.blindBox.service.BlindBagTypeService;
import com.product.server.hsf_301.blindBox.service.PrizeItemService;
import com.product.server.hsf_301.blindBox.service.SpinHistoryService;


import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/blindbox")
@RequiredArgsConstructor
public class BlindBoxController {

    private final BlindBagTypeService blindBagTypeService;
    private final PrizeItemService prizeItemService;
    private final SpinHistoryService spinHistoryService;


    
    // Directory where uploaded files will be stored

    @GetMapping("/")
    public String list(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size, Model model) {
        Page<BlindPackage> blindBagTypes = blindBagTypeService.getAllBlindBagTypes(page, size);
        model.addAttribute("blindBagTypes", blindBagTypes);

        System.out.println("OK");
        return "blindbox/list";
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable Integer id, Model model) {
        BlindPackage blindBagType = blindBagTypeService.getBlindBagTypeById(id);
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

}
