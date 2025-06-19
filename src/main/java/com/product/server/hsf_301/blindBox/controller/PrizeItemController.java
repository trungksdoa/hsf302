package com.product.server.hsf_301.blindBox.controller;

import com.product.server.hsf_301.blindBox.model.PackagesBox;
import com.product.server.hsf_301.blindBox.model.PrizeItem;
import com.product.server.hsf_301.blindBox.service.BlindBagTypeService;
import com.product.server.hsf_301.blindBox.service.PrizeItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/prizeItems")
public class PrizeItemController {

    private final PrizeItemService prizeItemService;
    private final BlindBagTypeService blindBagTypeService;

    @Autowired
    public PrizeItemController(PrizeItemService prizeItemService, BlindBagTypeService blindBagTypeService) {
        this.prizeItemService = prizeItemService;
        this.blindBagTypeService = blindBagTypeService;
    }

    @GetMapping
    public String getAllPrizeItems(Model model) {
        List<PrizeItem> prizeItems = prizeItemService.getAllPrizeItems();
        model.addAttribute("prizeItems", prizeItems);
        return "prizeItem/list";
    }

    @GetMapping("/{id}")
    public String getPrizeItemById(@PathVariable Integer id, Model model) {
        PrizeItem prizeItem = prizeItemService.getPrizeItemById(id);
        model.addAttribute("prizeItem", prizeItem);
        return "prizeItem/details";
    }

    @GetMapping("/box/{blindBoxId}")
    @ResponseBody
    public List<PrizeItem> getPrizesByBlindBoxId(@PathVariable String blindBoxId) {
        return prizeItemService.getAllPrizeItemsByBlindId(Integer.parseInt(blindBoxId));
    }

    @GetMapping("/view/{blindBoxId}")
    public String getPrizesByBlindId(@PathVariable Integer blindBoxId, Model model) {
        List<PrizeItem> prizeItems = prizeItemService.getAllPrizeItemsByBlindId(blindBoxId);
        model.addAttribute("prizeItems", prizeItems);
        return "prizeItem/details";
    }

    @GetMapping("/create")
    public String createPrizeItemForm(Model model) {
        List<PackagesBox> blindBagTypes = blindBagTypeService.getAllBlindBagTypes();
        model.addAttribute("prizeItem", new PrizeItem());
        model.addAttribute("blindBagTypes", blindBagTypes);
        return "prizeItem/create";
    }

    @PostMapping("/create")
    public String createPrizeItem(
            @ModelAttribute PrizeItem prizeItem,
            @RequestParam("imageFile") MultipartFile imageFile) {

        if (!imageFile.isEmpty()) {
            try {
                prizeItem.setImageData(imageFile.getBytes());
                prizeItem.setImageType(imageFile.getContentType());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        prizeItemService.savePrizeItem(prizeItem);
        return "redirect:/prizeItems";
    }

    @GetMapping("/edit/{id}")
    public String editPrizeItemForm(@PathVariable Integer id, Model model) {
        PrizeItem prizeItem = prizeItemService.getPrizeItemById(id);
        List<PackagesBox> blindBagTypes = blindBagTypeService.getAllBlindBagTypes();
        model.addAttribute("prizeItem", prizeItem);
        model.addAttribute("blindBagTypes", blindBagTypes);
        return "prizeItem/edit";
    }

    @PostMapping("/edit/{id}")
    public String updatePrizeItem(@PathVariable Integer id, @ModelAttribute PrizeItem prizeItem) {
        prizeItem.setItemId(id);
        prizeItemService.savePrizeItem(prizeItem);
        return "redirect:/prizeItems";
    }

    @GetMapping("/delete/{id}")
    public String deletePrizeItem(@PathVariable Integer id) {
        prizeItemService.deletePrizeItem(id);
        return "redirect:/prizeItems";
    }
    
    @GetMapping("/byBagType/{bagTypeId}")
    public String getPrizeItemsByBagType(@PathVariable Integer bagTypeId, Model model) {
        PackagesBox blindBagType = blindBagTypeService.getBlindBagTypeById(bagTypeId);
        List<PrizeItem> prizeItems = prizeItemService.getPrizeItemsByBlindBagType(blindBagType);
        model.addAttribute("prizeItems", prizeItems);
        model.addAttribute("blindBagType", blindBagType);
        return "prizeItem/byBagType";
    }
}
