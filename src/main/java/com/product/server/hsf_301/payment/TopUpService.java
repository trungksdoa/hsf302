package com.product.server.hsf_301.payment;

import com.product.server.hsf_301.payment.model.TopUpHistory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TopUpService  {
    private final TopUpRepository topUpRepository;

    public void save(TopUpHistory topUpHistory) {
        topUpRepository.save(topUpHistory);
    }

    public List<TopUpHistory> getAllTopUp() {
        return topUpRepository.findAllByUser_UserId(2);
    }


}
