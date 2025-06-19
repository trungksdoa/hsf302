package com.product.server.hsf_301.blindBox.service.impl;

import com.product.server.hsf_301.blindBox.model.UserPrizeItem;
import com.product.server.hsf_301.blindBox.repository.UserPrizeitemRepository;
import com.product.server.hsf_301.blindBox.service.OrderService;
import com.product.server.hsf_301.blindBox.service.UserPrizeItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class UserPrizeItemServiceImpl implements UserPrizeItemService {
    private final UserPrizeitemRepository userPrizeitemRepository;
    private final OrderService orderService;
    @Override
    @Transactional(readOnly = true)
    public Page<UserPrizeItem> getAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));
        return userPrizeitemRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserPrizeItem> getAll() {
        return userPrizeitemRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public UserPrizeItem getById(Long id) {
        return userPrizeitemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("UserPrizeItem not found with id: " + id));
    }

    @Transactional
    @Override
    public UserPrizeItem save(UserPrizeItem userPrizeItem) {
        if (userPrizeItem == null) {
            throw new IllegalArgumentException("UserPrizeItem cannot be null");
        }
        return userPrizeitemRepository.save(userPrizeItem);
    }

    @Transactional
    @Override
    public void deleteById(Long id) {
        if (!userPrizeitemRepository.existsById(id)) {
            throw new RuntimeException("UserPrizeItem not found with id: " + id);
        }
        userPrizeitemRepository.deleteById(id);
    }

    // Additional useful methods for UserPrizeItem management
    @Transactional(readOnly = true)
    @Override
    public Page<UserPrizeItem> getByUserId(int userId, int page,int size) {
        Pageable pg = PageRequest.of(page,size);
        return userPrizeitemRepository.findByUser_UserId(userId,pg);
    }
    @Transactional(readOnly = true)
    @Override
    public List<UserPrizeItem> getByUserIdAndClaimed(int userId, boolean isClaimed) {
        return userPrizeitemRepository.findByUser_UserIdAndClaimed(userId, isClaimed);
    }

    @Transactional
    @Override
    public UserPrizeItem claimPrize(Long userPrizeItemId) {
        UserPrizeItem userPrizeItem = getById(userPrizeItemId);

        if (userPrizeItem.isClaimed()) {
            throw new RuntimeException("Prize has already been claimed");
        }

        if (!userPrizeItem.getSpin().getPrizeItemId().isClaimAble()) {
            throw new RuntimeException("This prize is not claimable");
        }

        userPrizeItem.setClaimed(true);
        userPrizeItem.setClaimedAt(java.time.LocalDateTime.now());
        orderService.saveOrder(userPrizeItem);
        return userPrizeitemRepository.save(userPrizeItem);
    }

    @Transactional
    @Override
    public void claimPrize(List<Long> prizeItemId) {
        prizeItemId.forEach(item ->{
            UserPrizeItem userPrizeItem = getById(item);

            if (userPrizeItem.isClaimed()) {
                throw new RuntimeException("Prize has already been claimed");
            }

            if (!userPrizeItem.getSpin().getPrizeItemId().isClaimAble()) {
                throw new RuntimeException("This prize is not claimable");
            }

            userPrizeItem.setClaimed(true);
            userPrizeItem.setActive(false);
            userPrizeItem.setClaimedAt(LocalDateTime.now());
            orderService.saveOrder(userPrizeItem);
            userPrizeitemRepository.save(userPrizeItem);
        });
    }
//
//    @Transactional(readOnly = true)
//    public List<UserPrizeItem> getByPrizeItemId(Integer prizeItemId) {
//        return userPrizeitemRepository.findByPrizeItemId(prizeItemId);
//    }
//
//    @Transactional(readOnly = true)
//    public Page<UserPrizeItem> getByUserId(Integer userId, int page, int size) {
//        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "wonDate"));
//        return userPrizeitemRepository.findByUserId(userId, pageable);
//    }
//
//    public void deleteByUserId(Integer userId) {
//        userPrizeitemRepository.deleteByUserId(userId);
//    }
}