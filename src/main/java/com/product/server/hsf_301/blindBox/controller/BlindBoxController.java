package com.product.server.hsf_301.blindBox.controller;

import com.product.server.hsf_301.blindBox.model.BlindBagType;
import com.product.server.hsf_301.blindBox.model.PrizeItem;
import com.product.server.hsf_301.blindBox.repository.BlindBoxRepository;
import com.product.server.hsf_301.blindBox.repository.PrizeItemRepository;
import com.product.server.hsf_301.blindBox.service.BlindBagTypeService;
import com.product.server.hsf_301.blindBox.service.PrizeItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/blindbox")
@RequiredArgsConstructor
public class BlindBoxController {

    private final BlindBagTypeService blindBagTypeService;

    private final PrizeItemService prizeItemService;

    @GetMapping("/ ")
    public String list(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size, Model model) {
        Page<BlindBagType> blindBagTypes = blindBagTypeService.getAllBlindBagTypes(page, size);
        model.addAttribute("blindBagTypes", blindBagTypes);

        System.out.println("OK");
        return "blindbox/list";
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable Integer id, Model model) {
        BlindBagType blindBagType = blindBagTypeService.getBlindBagTypeById(id);
        model.addAttribute("blindBagType", blindBagType);
        return "blindBagType/details";
    }

    // API endpoint to get possible items for a blind box
    @GetMapping("/{id}/items")
    @ResponseBody
    public ResponseEntity<List<PrizeItem>> getPossibleItems(@PathVariable Integer id) {
        BlindBagType blindBagType = blindBagTypeService.getBlindBagTypeById(id);
        if(blindBagType == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        List<PrizeItem> items = prizeItemService.getPrizeItemByBlindBoxAndActive(blindBagType);
        return ResponseEntity.ok(items);
    }

    // Handle purchase
    @PostMapping("/{id}/purchase")
    @ResponseBody
    public ResponseEntity<?> purchaseBlindBox(@PathVariable Integer id) {
        BlindBagType blindBagType = blindBagTypeService.getBlindBagTypeById(id);

        if(blindBagType == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        return ResponseEntity.ok().body("{\"status\":\"success\",\"message\":\"Purchase initiated\"}");

    }

    // Redirect to payment page
    @GetMapping("/{id}/purchase")
    public String purchasePage(@PathVariable Integer id, Model model) {
        BlindBagType blindBagType = blindBagTypeService.getBlindBagTypeById(id);
        if(blindBagType == null) return "redirect:/blindbox/list";

        model.addAttribute("blindBagType", blindBagType);
        return "blindbox/purchase";
    }



    @GetMapping("/create")
    public String createBlindBagTypeForm(Model model) {
        model.addAttribute("blindBagType", new BlindBagType());
        return "blindBagType/create";
    }

    @PostMapping("/create")
    public String createBlindBagType(@ModelAttribute BlindBagType blindBagType) {
        blindBagTypeService.saveBlindBagType(blindBagType);
        return "redirect:/blindBagTypes";
    }

    @GetMapping("/edit/{id}")
    public String editBlindBagTypeForm(@PathVariable Integer id, Model model) {
        BlindBagType blindBagType = blindBagTypeService.getBlindBagTypeById(id);
        model.addAttribute("blindBagType", blindBagType);
        return "blindBagType/edit";
    }

    @PostMapping("/edit/{id}")
    public String updateBlindBagType(@PathVariable Integer id, @ModelAttribute BlindBagType blindBagType) {
        blindBagType.setBagTypeId(id);
        blindBagTypeService.saveBlindBagType(blindBagType);
        return "redirect:/blindBagTypes";
    }

    @GetMapping("/delete/{id}")
    public String deleteBlindBagType(@PathVariable Integer id) {
        blindBagTypeService.deleteBlindBagType(id);
        return "redirect:/blindBagTypes";
    }
}
