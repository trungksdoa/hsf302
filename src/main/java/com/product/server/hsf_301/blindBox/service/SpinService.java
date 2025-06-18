package com.product.server.hsf_301.blindBox.service;


import com.product.server.hsf_301.blindBox.model.PackagesBox;
import com.product.server.hsf_301.blindBox.model.SpinHistory;
import com.product.server.hsf_301.blindBox.model.AppUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SpinService {
    private final SpinHistoryService spinHistoryService;
    private final UserService userService;


    public SpinHistory spinItem(AppUser user, PackagesBox blindPackage){
        try{
            SpinHistory spinHistory = spinHistoryService.spin(user,blindPackage);
            user.setBalance(
                    user.getBalance().subtract(BigDecimal.valueOf(spinHistory.getPrice()))
            );
            userService.updateProfile(user);

            return spinHistory;
        }catch (Exception e){
                throw  new RuntimeException(e.getMessage());
        }
    }

    public List<SpinHistory> spinItems(AppUser user, PackagesBox blindPackage, int number){
        try{
            List<SpinHistory> spinHistory = spinHistoryService.spinMultiple(user,blindPackage,number);

            spinHistory.forEach(spinHistoryItem -> {
                user.setBalance(
                        user.getBalance().subtract(BigDecimal.valueOf(spinHistoryItem.getPrice()))
                );
                userService.updateProfile(user);
            });


            return spinHistory;
        }catch (Exception e){
            throw  new RuntimeException(e.getMessage());
        }
    }
}
