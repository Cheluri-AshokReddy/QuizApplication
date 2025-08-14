package com.quizserver.quizserver.Service;

import com.quizserver.quizserver.DTO.*;
import com.quizserver.quizserver.Model.Question;
import com.quizserver.quizserver.Model.Test;
import com.quizserver.quizserver.Model.TestResult;
import com.quizserver.quizserver.Model.User;
import com.quizserver.quizserver.Repository.QuestionRepository;
import com.quizserver.quizserver.Repository.TestRepository;
import com.quizserver.quizserver.Repository.TestResultRepository;
import com.quizserver.quizserver.Repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
public class TestServiceImpl implements TestService {

    private final TestRepository testRepository;
    private final ModelMapper modelMapper;
    private final QuestionRepository questionRepository;
    private final TestResultRepository testResultRepository;
    private final UserRepository userRepository;


    public TestDTO createTest(TestDTO dto){

        Test test = modelMapper.map(dto, Test.class);
        testRepository.save(test);
        return modelMapper.map(test, TestDTO.class);
    }

    public QuestionDTO addQuestionInTest(QuestionDTO dto) {
        Optional<Test> optionalTest = testRepository.findById(dto.getId()); // Make sure this is testId
        if (optionalTest.isPresent()) {
            Question question = modelMapper.map(dto, Question.class);
            question.setId(null);
            question.setTest(optionalTest.get());
            questionRepository.save(question);
            return modelMapper.map(question, QuestionDTO.class);
        }
        throw new EntityNotFoundException("Test Not Found");
    }

    public List<TestDTO> getAllTests() {
        return testRepository.findAll()
                .stream()
                .peek(test -> test.setTime(test.getQuestions().size() * test.getTime()))
                .collect(toList()).stream().map(test -> modelMapper.map(test, TestDTO.class)).collect(toList());
    }


    public TestDetailsDTO getAllQuestionsByTest(Long id) {
        Optional<Test> optionalTest = testRepository.findById(id);
        TestDetailsDTO testDetailsDTO = new TestDetailsDTO();

        if (optionalTest.isPresent()) {
            TestDTO testDTO = modelMapper.map(optionalTest.get(), TestDTO.class);
            testDTO.setTime(optionalTest.get().getTime() * optionalTest.get().getQuestions().size());

            testDetailsDTO.setTestDTO(testDTO);
            testDetailsDTO.setQuestions(
                    optionalTest.get().getQuestions().stream()
                            .map(test->modelMapper.map(test,QuestionDTO.class))
                            .toList()
            );
        }
        return testDetailsDTO;
    }

    public TestResultDTO submitTest(SubmitTestDTO request) {
        Test test = testRepository.findById(request.getTestId())
                .orElseThrow(() -> new EntityNotFoundException("Test not found"));

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        int correctAnswers = 0;
        for (QuestionResponse response : request.getResponses()) {
            Question question = questionRepository.findById(response.getQuestionId())
                    .orElseThrow(() -> new EntityNotFoundException("Question not found"));

            if (Objects.equals(question.getCorrectOption(), response.getSelectedOption())) {
                correctAnswers++;
            }
        }

        int totalQuestions = test.getQuestions().size();
        double percentage = totalQuestions == 0 ? 0.0 : ((double) correctAnswers / totalQuestions) * 100.0;

        TestResult testResult = new TestResult();
        testResult.setTest(test);
        testResult.setUser(user);
        testResult.setTotalQuestions(totalQuestions);
        testResult.setCorrectAnswers(correctAnswers);
        testResult.setPercentage(percentage);
        testResult.setTestName(test.getTitle());
        testResultRepository.save(testResult);
        TestResultDTO testResultDTO = modelMapper.map(testResult, TestResultDTO.class);
        testResultDTO.setTestName(test.getTitle());
        return testResultDTO;
    }

    public List<TestResultDTO> getAllTestResults() {
        List<TestResult>testResults = testResultRepository.findAll();
        List<TestResultDTO>testResultDTOS = new ArrayList<>();
        for (TestResult testResult : testResults) {
            TestResultDTO testResultDTO = modelMapper.map(testResult, TestResultDTO.class);
            testResultDTO.setTestName(testResult.getTest().getTitle());
            testResultDTOS.add(testResultDTO);
        }
        return testResultDTOS;
    }

    public List<TestResultDTO> getAllTestResultsOfUser(Long userId) {
        List<TestResult>testResults= testResultRepository.findAllByUserId(userId);
        List<TestResultDTO>testResultDTOS = new ArrayList<>();
        for (TestResult testResult : testResults) {
            TestResultDTO testResultDTO = modelMapper.map(testResult, TestResultDTO.class);
            testResultDTO.setTestName(testResult.getTest().getTitle());
            testResultDTOS.add(testResultDTO);
        }
        return testResultDTOS;
    }

}
