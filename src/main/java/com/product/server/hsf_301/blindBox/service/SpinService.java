package com.product.server.hsf_301.blindBox.service;


import com.product.server.hsf_301.blindBox.model.PackagesBox;
import com.product.server.hsf_301.blindBox.model.SpinHistory;
import com.product.server.hsf_301.blindBox.model.UserPrizeItem;
import com.product.server.hsf_301.user.model.AppUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SpinService {
    private final SpinHistoryService spinHistoryService;
    private final UserService userService;
    private final OrderService orderService;
    private final UserPrizeItemService userPrizeItemService;

    public SpinHistory spinItem(AppUser user, PackagesBox blindPackage){
        try{
            SpinHistory spinHistory = spinHistoryService.spin(user,blindPackage);

            if(user.getBalance().compareTo(BigDecimal.valueOf(spinHistory.getPrice())) < 0){
                throw new RuntimeException("Không đủ tiền");
            }

            user.setBalance(
                    user.getBalance().subtract(BigDecimal.valueOf(spinHistory.getPrice()))
            );

            UserPrizeItem item = new UserPrizeItem();
            item.setUser(user);
            item.setClaimed(false);
            item.setActive(true);
            item.setSpin(spinHistory);
            userPrizeItemService.save(item);
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
