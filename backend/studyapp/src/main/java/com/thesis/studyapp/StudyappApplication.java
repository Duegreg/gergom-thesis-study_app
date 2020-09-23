package com.thesis.studyapp;

import com.thesis.studyapp.model.Group;
import com.thesis.studyapp.model.Task;
import com.thesis.studyapp.model.TaskAnswer;
import com.thesis.studyapp.model.Test;
import com.thesis.studyapp.model.TestTask;
import com.thesis.studyapp.model.User;
import com.thesis.studyapp.model.UserTestStatus;
import com.thesis.studyapp.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;

@SpringBootApplication
public class StudyappApplication {
    public static boolean TEST_DATA = true;

    public static void main(String[] args) {
        SpringApplication.run(StudyappApplication.class, args);
    }

    @Bean
    CommandLineRunner demo(UserRepository userRepository) {
        return args -> {
            if (!TEST_DATA)
                return;
            if (userRepository.count() != 0)
                return;

            User user = User.builder()
                    .code("User")
                    .name("UUUU0001")
                    .build();
            User user2 = User.builder()
                    .code("User2")
                    .name("UUUU0002")
                    .build();
            User user3 = User.builder()
                    .code("User3")
                    .name("UUUU0003")
                    .build();
            User user4 = User.builder()
                    .code("User4")
                    .name("UUUU0004")
                    .build();
            user.addParent(user4);

            Group group = Group.builder()
                    .name("Group")
                    .description("group 1 init disc")
                    .news("News!")
                    .newsChangedDate(ZonedDateTime.now())
                    .code("GGGG0001")
                    .build();
            Group group2 = Group.builder()
                    .name("Group2")
                    .description("group 2 init disc")
                    .news("News 2!")
                    .newsChangedDate(ZonedDateTime.now())
                    .code("GGGG0002")
                    .build();
            user.addTeacherGroup(group);
            user.addTeacherGroup(group2);
            user.addStudentGroup(group);
            user.addStudentGroup(group2);
            user2.addTeacherGroup(group);
            user2.addStudentGroup(group);
            user2.addStudentGroup(group2);
            user3.addStudentGroup(group);
            user4.addStudentGroup(group2);

            Set<TaskAnswer> answers = new HashSet<>();
            answers.add(TaskAnswer.builder().number(1).answer("Answer 1 is here").build());
            answers.add(TaskAnswer.builder().number(2)
                    .answer("Answer 2 is a little bit longer than answer 1 if you don't mind (and lets have some extra %/=* character too").build());
            answers.add(TaskAnswer.builder().number(3).answer("3.").build());
            answers.add(TaskAnswer.builder().number(4).answer("This is only for testing").build());

            Task task = Task.builder()
                    .question("Task1, the soltuion is the number 1")
                    .solutionNumber(1)
                    .answers(answers)
                    .usage(1L)
                    .build();
            Task task2 = Task.builder()
                    .question("Task2 is a little bit longer for making the development testing easier for me. Solution is number 2!")
                    .solutionNumber(2)
                    .answers(answers)
                    .usage(2L)
                    .build();

            Test test = Test.builder()
                    .name("Test")
                    .description("Test1 desc. about something interesting")
                    .build();
            TestTask testTask = TestTask.builder()
                    .level(4)
                    .task(task)
                    .allAnswers(5)
                    .correctAnswers(3)
                    .build();
            TestTask testTask2 = TestTask.builder()
                    .level(1)
                    .task(task2)
                    .allAnswers(6)
                    .correctAnswers(4)
                    .build();
            test.addTask(testTask);
            test.addTask(testTask2);
            group.addTest(test);

            UserTestStatus userTestStatus = UserTestStatus.builder()
                    .status(UserTestStatus.Status.IN_PROGRESS)
                    .statusChangedDate(ZonedDateTime.now(ZoneOffset.UTC))
                    .user(user)
                    .test(test)
                    .allAnswers(56)
                    .correctAnswers(18)
                    .currentCycle(1)
                    .currentLevel(1)
                    .correctAnswersInRow(2)
                    .build();

            test.addUserTestStatus(userTestStatus);
            user.addUserTestStatus(userTestStatus);

            userRepository.save(user);
            userRepository.save(user2);
            userRepository.save(user3);
            userRepository.save(user4);
        };
    }
}
