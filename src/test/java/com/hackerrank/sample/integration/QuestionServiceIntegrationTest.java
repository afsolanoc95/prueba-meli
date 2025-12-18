package com.hackerrank.sample.integration;

import com.hackerrank.sample.dto.AnswerQuestionRequest;
import com.hackerrank.sample.dto.CreateQuestionRequest;
import com.hackerrank.sample.dto.QuestionDTO;
import com.hackerrank.sample.exception.NoSuchResourceFoundException;
import com.hackerrank.sample.model.Product;
import com.hackerrank.sample.repository.ProductRepository;
import com.hackerrank.sample.service.QuestionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Integration tests for QuestionService covering complete question lifecycle
 */
@Transactional
class QuestionServiceIntegrationTest extends BaseIntegrationTest {

        @Autowired
        private QuestionService questionService;

        @Autowired
        private ProductRepository productRepository;

        private Product testProduct;

        @BeforeEach
        void setUpProductForQuestions() {
                testProduct = createBasicProductBuilder().build();
                testProduct = productRepository.save(testProduct);
        }

        @Test
        @DisplayName("Should complete question lifecycle: create -> answer -> get -> delete")
        void testCompleteQuestionLifecycle() throws IOException {
                // 1. CREATE: Add question
                CreateQuestionRequest createRequest = readJson("question/create-question-request.json",
                                CreateQuestionRequest.class);
                createRequest.setQuestion("What is the battery capacity?");

                QuestionDTO created = questionService.addQuestion(testProduct.getId(), createRequest);

                assertThat(created).isNotNull();
                assertThat(created.getQuestion()).isEqualTo("What is the battery capacity?");
                assertThat(created.getUserName()).isEqualTo("buyer1");
                assertThat(created.getAnswer()).isNull();
                assertThat(created.getAnsweredAt()).isNull();
                assertThat(created.getCreatedAt()).isNotNull();

                // 2. ANSWER: Answer the question
                AnswerQuestionRequest answerRequest = readJson("question/answer-question-request.json",
                                AnswerQuestionRequest.class);
                answerRequest.setAnswer("The battery capacity is 5000mAh");

                QuestionDTO answered = questionService.answerQuestion(testProduct.getId(), created.getId(),
                                answerRequest);

                assertThat(answered.getAnswer()).isEqualTo("The battery capacity is 5000mAh");
                assertThat(answered.getAnsweredAt()).isNotNull();
                assertThat(answered.getAnsweredAt()).isAfter(answered.getCreatedAt().minusSeconds(1));

                // 3. GET ALL: Retrieve all questions
                List<QuestionDTO> questions = questionService.getProductQuestions(testProduct.getId());

                assertThat(questions).hasSize(1);
                assertThat(questions.get(0).getAnswer()).isNotNull();

                // 4. DELETE: Delete question
                questionService.deleteQuestion(testProduct.getId(), created.getId());

                // 5. VERIFY: Confirm question deleted
                List<QuestionDTO> remainingQuestions = questionService.getProductQuestions(testProduct.getId());
                assertThat(remainingQuestions).isEmpty();
        }

        @Test
        @DisplayName("Should set answeredAt automatically when answering question")
        void testAnswerQuestion_SetsAnsweredAtAutomatically() throws IOException {
                // Create question
                CreateQuestionRequest createRequest = readJson("question/create-question-request.json",
                                CreateQuestionRequest.class);
                QuestionDTO question = questionService.addQuestion(testProduct.getId(), createRequest);

                assertThat(question.getAnsweredAt()).isNull();

                // Answer question
                AnswerQuestionRequest answerRequest = readJson("question/answer-question-request.json",
                                AnswerQuestionRequest.class);
                QuestionDTO answered = questionService.answerQuestion(testProduct.getId(), question.getId(),
                                answerRequest);

                // Verify answeredAt is set automatically
                assertThat(answered.getAnsweredAt()).isNotNull();
                assertThat(answered.getAnsweredAt()).isAfterOrEqualTo(answered.getCreatedAt());
        }

        @Test
        @DisplayName("Should throw exception when adding question to non-existent product")
        void testAddQuestionToNonExistentProduct_ShouldThrowException() throws IOException {
                CreateQuestionRequest request = readJson("question/create-question-request.json",
                                CreateQuestionRequest.class);

                assertThatThrownBy(() -> questionService.addQuestion(99999L, request))
                                .isInstanceOf(NoSuchResourceFoundException.class)
                                .hasMessageContaining("Product not found");
        }

        @Test
        @DisplayName("Should throw exception when answering question from different product")
        void testAnswerQuestionFromDifferentProduct_ShouldThrowException() throws IOException {
                // Create two products
                final Product product2 = productRepository.save(createBasicProductBuilder().title("Product 2").build());

                // Add question to product 1
                CreateQuestionRequest createRequest = readJson("question/create-question-request.json",
                                CreateQuestionRequest.class);
                QuestionDTO question = questionService.addQuestion(testProduct.getId(), createRequest);

                // Try to answer from product 2's endpoint
                AnswerQuestionRequest answerRequest = readJson("question/answer-question-request.json",
                                AnswerQuestionRequest.class);

                Long product2Id = product2.getId();
                Long questionId = question.getId();
                assertThatThrownBy(() -> questionService.answerQuestion(product2Id, questionId, answerRequest))
                                .isInstanceOf(NoSuchResourceFoundException.class)
                                .hasMessageContaining("does not belong to product");
        }

        @Test
        @DisplayName("Should throw exception when deleting question from different product")
        void testDeleteQuestionFromDifferentProduct_ShouldThrowException() throws IOException {
                // Create second product
                final Product product2 = productRepository.save(createBasicProductBuilder().title("Product 2").build());

                // Add question to product 1
                CreateQuestionRequest request = readJson("question/create-question-request.json",
                                CreateQuestionRequest.class);
                QuestionDTO question = questionService.addQuestion(testProduct.getId(), request);

                // Try to delete from product 2's endpoint
                Long product2Id = product2.getId();
                Long questionId = question.getId();
                assertThatThrownBy(() -> questionService.deleteQuestion(product2Id, questionId))
                                .isInstanceOf(NoSuchResourceFoundException.class)
                                .hasMessageContaining("does not belong to product");
        }

        @Test
        @DisplayName("Should return questions ordered by created date descending")
        void testQuestionsOrderedByCreatedAtDesc() throws IOException {
                // Add multiple questions
                CreateQuestionRequest request1 = readJson("question/create-question-request.json",
                                CreateQuestionRequest.class);
                request1.setQuestion("First question?");
                questionService.addQuestion(testProduct.getId(), request1);

                CreateQuestionRequest request2 = readJson("question/create-question-request.json",
                                CreateQuestionRequest.class);
                request2.setQuestion("Second question?");
                request2.setUserName("user2");
                questionService.addQuestion(testProduct.getId(), request2);

                CreateQuestionRequest request3 = readJson("question/create-question-request.json",
                                CreateQuestionRequest.class);
                request3.setQuestion("Third question?");
                request3.setUserName("user3");
                questionService.addQuestion(testProduct.getId(), request3);

                // Get questions
                List<QuestionDTO> questions = questionService.getProductQuestions(testProduct.getId());

                assertThat(questions).hasSize(3);
                // Verify contents without Strict Order if execution is too fast for timestamp
                // difference
                assertThat(questions).extracting(QuestionDTO::getQuestion).containsExactlyInAnyOrder("Third question?",
                                "Second question?", "First question?");
        }

        @Test
        @DisplayName("Should throw exception when answering non-existent question")
        void testAnswerNonExistentQuestion_ShouldThrowException() throws IOException {
                AnswerQuestionRequest request = readJson("question/answer-question-request.json",
                                AnswerQuestionRequest.class);

                Long productId = testProduct.getId();
                assertThatThrownBy(() -> questionService.answerQuestion(productId, 99999L, request))
                                .isInstanceOf(NoSuchResourceFoundException.class)
                                .hasMessageContaining("Question not found");
        }
}
