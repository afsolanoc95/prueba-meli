package com.hackerrank.sample.mapper;

import com.hackerrank.sample.dto.ReviewDTO;
import com.hackerrank.sample.model.Review;
import org.springframework.stereotype.Component;

@Component
public class ReviewMapper {

    public ReviewDTO toDTO(Review review) {
        return ReviewDTO.builder().id(review.getId()).rating(review.getRating()).comment(review.getComment())
                .userName(review.getUserName()).createdAt(review.getCreatedAt()).build();
    }
}
