package com.hackerrank.sample.service;

import com.hackerrank.sample.dto.AnswerQuestionRequest;
import com.hackerrank.sample.dto.CreateQuestionRequest;
import com.hackerrank.sample.dto.QuestionDTO;
import com.hackerrank.sample.exception.NoSuchResourceFoundException;
import com.hackerrank.sample.mapper.QuestionMapper;
import com.hackerrank.sample.model.Product;
import com.hackerrank.sample.model.Question;
import com.hackerrank.sample.repository.ProductRepository;
import com.hackerrank.sample.repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class QuestionService {

    private final QuestionRepository questionRepository;
    private final ProductRepository productRepository;
    private final QuestionMapper questionMapper;

    public QuestionService(QuestionRepository questionRepository, ProductRepository productRepository,
            QuestionMapper questionMapper) {
        this.questionRepository = questionRepository;
        this.productRepository = productRepository;
        this.questionMapper = questionMapper;
    }

    @Transactional(readOnly = true)
    @io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker(name = "questionCB")
    public List<QuestionDTO> getProductQuestions(Long productId) {
        log.debug("Fetching questions for product id: {}", productId);
        if (!productRepository.existsById(productId)) {
            throw new NoSuchResourceFoundException("Product not found with id: " + productId);
        }

        List<Question> questions = questionRepository.findByProductIdOrderByCreatedAtDesc(productId);
        return questions.stream().map(questionMapper::toDTO).toList();
    }

    @Transactional
    public QuestionDTO addQuestion(Long productId, CreateQuestionRequest request) {
        log.debug("Adding question to product id: {}", productId);
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new NoSuchResourceFoundException("Product not found with id: " + productId));

        Question question = Question.builder().question(request.getQuestion()).userName(request.getUserName())
                .product(product).build();
        // JPA PrePersist handles default createdAt

        Question savedQuestion = questionRepository.save(question);
        log.info("Question added to product id: {}", productId);
        return questionMapper.toDTO(savedQuestion);
    }

    @Transactional
    @io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker(name = "questionCB")
    public QuestionDTO answerQuestion(Long productId, Long questionId, AnswerQuestionRequest request) {
        log.debug("Answering question id: {}", questionId);
        if (!productRepository.existsById(productId)) {
            throw new NoSuchResourceFoundException("Product not found with id: " + productId);
        }

        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new NoSuchResourceFoundException("Question not found with id: " + questionId));

        if (!question.getProduct().getId().equals(productId)) {
            throw new NoSuchResourceFoundException(
                    "Question " + questionId + " does not belong to product " + productId);
        }

        question.setAnswer(request.getAnswer());
        question.setAnsweredAt(LocalDateTime.now());

        Question answeredQuestion = questionRepository.save(question);
        log.info("Question answered, id: {}", questionId);
        return questionMapper.toDTO(answeredQuestion);
    }

    @Transactional
    @io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker(name = "questionCB")
    public void deleteQuestion(Long productId, Long questionId) {
        log.debug("Deleting question id: {}", questionId);
        if (!productRepository.existsById(productId)) {
            throw new NoSuchResourceFoundException("Product not found with id: " + productId);
        }

        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new NoSuchResourceFoundException("Question not found with id: " + questionId));

        if (!question.getProduct().getId().equals(productId)) {
            throw new NoSuchResourceFoundException(
                    "Question " + questionId + " does not belong to product " + productId);
        }

        questionRepository.deleteById(questionId);
        log.info("Question deleted, id: {}", questionId);
    }
}
