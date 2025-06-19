package com.product.server.hsf_301.blindBox.controller;


import com.product.server.hsf_301.blindBox.model.UserPrizeItem;
import com.product.server.hsf_301.blindBox.service.UserPrizeItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/user-prizes")
@RequiredArgsConstructor
public class UserPrizeController {
    private final UserPrizeItemService userPrizeItemService;

    @PostMapping("/bulk-claim")
    public ResponseEntity<Map<String, Object>> bulkClaim(@RequestBody Map<String, List<String>> request
    ) {
        Map<String, Object> response = new HashMap<>();

        try {
            List<String> prizeIdStrings = request.get("prizeIds");
            if (prizeIdStrings == null || prizeIdStrings.isEmpty()) {
                response.put("success", false);
                response.put("message", "No prize IDs provided");
                return ResponseEntity.badRequest().body(response);
            }

            // Convert String IDs to Long IDs
            List<Long> prizeIds = prizeIdStrings.stream()
                    .map(Long::valueOf)
                    .collect(Collectors.toList());

            // Call service with Long IDs
            userPrizeItemService.claimPrize(prizeIds);

            response.put("success", true);
            response.put("message", "Prizes claimed successfully");
            return ResponseEntity.ok(response);

        } catch (NumberFormatException e) {
            response.put("success", false);
            response.put("message", "Invalid prize ID format");
            return ResponseEntity.badRequest().body(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error claiming prizes: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }
    @PostMapping("/{prizeId}/claim")
    public ResponseEntity<Map<String, Object>> claim(@PathVariable("prizeId") Long prizeId) {
        Map<String, Object> response = new HashMap<>();

        try {
            UserPrizeItem claimedPrize = userPrizeItemService.claimPrize(prizeId);

            response.put("success", true);
            response.put("message", "Prize claimed successfully");
            response.put("claimedPrize", claimedPrize);

            return ResponseEntity.ok(response);

        } catch (RuntimeException e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);

        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "An unexpected error occurred while claiming the prize");
            return ResponseEntity.internalServerError().body(response);
        }
    }

}
