package com.hackerrank.sample.mapper;

import com.hackerrank.sample.dto.QuestionDTO;
import com.hackerrank.sample.model.Question;
import org.springframework.stereotype.Component;

@Component
public class QuestionMapper {

    public QuestionDTO toDTO(Question question) {
        return QuestionDTO.builder().id(question.getId()).question(question.getQuestion()).answer(question.getAnswer())
                .userName(question.getUserName()).createdAt(question.getCreatedAt())
                .answeredAt(question.getAnsweredAt()).build();
    }
}
