package com.product.server.hsf_301.blindBox.controller;

import com.product.server.hsf_301.blindBox.service.CollectionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/collection")
@RequiredArgsConstructor
public class CollectionController {

    private final CollectionService collectionService;

    @PostMapping("/add")
    public ResponseEntity<?> addToCollection(@RequestBody Map<String, Integer> request) {
        try {
            Integer userId = request.get("userId");
            Integer blindPackageId = request.get("blindPackageId");
            
            collectionService.addToCollection(userId, blindPackageId);
            
            return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Added to collection successfully"
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", e.getMessage()
            ));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of(
                "success", false,
                "message", "An error occurred while adding to collection"
            ));
        }
    }

    @DeleteMapping("/remove")
    public ResponseEntity<?> removeFromCollection(@RequestBody Map<String, Integer> request) {
        try {
            Integer userId = request.get("userId");
            Integer blindPackageId = request.get("blindPackageId");
            
            collectionService.removeFromCollection(userId, blindPackageId);
            
            return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Removed from collection successfully"
            ));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of(
                "success", false,
                "message", "An error occurred while removing from collection"
            ));
        }
    }

    @GetMapping("/check")
    public ResponseEntity<?> checkInCollection(@RequestParam Integer userId, @RequestParam Integer blindPackageId) {
        try {
            boolean inCollection = collectionService.isInCollection(userId, blindPackageId);
            
            return ResponseEntity.ok(Map.of(
                "success", true,
                "inCollection", inCollection
            ));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of(
                "success", false,
                "message", "An error occurred while checking collection"
            ));
        }
    }
}
