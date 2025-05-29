package com.product.server.hsf_301.blindBox.controller;

import com.product.server.hsf_301.blindBox.model.BlindPackage;
import com.product.server.hsf_301.blindBox.model.PrizeItem;
import com.product.server.hsf_301.blindBox.service.BlindBagTypeService;
import com.product.server.hsf_301.blindBox.service.PrizeItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/blindbox")
@RequiredArgsConstructor
public class BlindBoxController {

    private final BlindBagTypeService blindBagTypeService;
    private final PrizeItemService prizeItemService;
    
    // Directory where uploaded files will be stored

    @GetMapping("/ ")
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

    // API endpoint to get possible items for a blind box
    @GetMapping("/{id}/items")
    @ResponseBody
    public ResponseEntity<List<PrizeItem>> getPossibleItems(@PathVariable Integer id) {
        BlindPackage blindBagType = blindBagTypeService.getBlindBagTypeById(id);
        if(blindBagType == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        List<PrizeItem> items = prizeItemService.getPrizeItemByBlindBoxAndActive(blindBagType);
        return ResponseEntity.ok(items);
    }

    // Handle purchase
    @PostMapping("/{id}/purchase")
    @ResponseBody
    public ResponseEntity<?> purchaseBlindBox(@PathVariable Integer id) {
        BlindPackage blindBagType = blindBagTypeService.getBlindBagTypeById(id);

        if(blindBagType == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        return ResponseEntity.ok().body("{\"status\":\"success\",\"message\":\"Purchase initiated\"}");

    }

    // Redirect to payment page
    @GetMapping("/{id}/purchase")
    public String purchasePage(@PathVariable Integer id, Model model) {
        BlindPackage blindBagType = blindBagTypeService.getBlindBagTypeById(id);
        if(blindBagType == null) return "redirect:/blindbox/list";

        model.addAttribute("blindBagType", blindBagType);
        return "blindbox/purchase";
    }

}
