package com.thesis.studyapp.resolver.object;

import com.coxautodev.graphql.tools.GraphQLResolver;
import com.thesis.studyapp.dto.GroupDto;
import com.thesis.studyapp.dto.TestDto;
import com.thesis.studyapp.dto.TestTaskDto;
import com.thesis.studyapp.dto.UserTestStatusDto;
import com.thesis.studyapp.service.TestTaskService;
import com.thesis.studyapp.service.UserTestStatusService;
import lombok.RequiredArgsConstructor;
import org.dataloader.DataLoader;
import org.dataloader.DataLoaderRegistry;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Component
@RequiredArgsConstructor
public class TestResolver implements GraphQLResolver<TestDto> {

    private final UserTestStatusService userTestStatusService;
    private final TestTaskService testTaskService;
    private final DataLoaderRegistry dataLoaderRegistry;

    public CompletableFuture<List<TestTaskDto>> testTasks(TestDto testDTO) {
        return CompletableFuture.supplyAsync(() ->
                testTaskService.getTestTasksForTest(testDTO.getId())
        );
    }

    public CompletableFuture<List<UserTestStatusDto>> userTestStatuses(TestDto testDTO) {
        return CompletableFuture.supplyAsync(() ->
                userTestStatusService.getUserTestStatusesForTest(testDTO.getId())
        );
    }

    public CompletableFuture<GroupDto> group(TestDto testDto) {
        DataLoader<Long, GroupDto> groupLoader = dataLoaderRegistry.getDataLoader("groupLoader");
        return groupLoader.load(testDto.getGroupId());
    }
}