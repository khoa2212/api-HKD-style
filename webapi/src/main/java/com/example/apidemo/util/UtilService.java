package com.example.apidemo.util;

import com.example.apidemo.review.dto.ReviewDTO;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UtilService {

    public double calculateAverageRating(List<ReviewDTO> reviews) {
        if(reviews.isEmpty()) return 0;

        int sum = 0;
        for(ReviewDTO reviewDTO : reviews) {
            sum = sum + reviewDTO.getRating();
        }

        return sum * 1.0 / reviews.size();
    }

    public User getUserFromContext() {
        Object obj = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return (User) obj;
    }
}
