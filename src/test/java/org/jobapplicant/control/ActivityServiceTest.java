package org.jobapplicant.control;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.jobapplicant.entity.JobApplicant;
import org.jobapplicant.entity.TaskDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ActivityServiceTest {

    @InjectMocks
    ActivityService activityService;

    @Mock
    RuntimeService runtimeService;

    @Mock
    TaskService taskService;

    @Mock
    RepositoryService repositoryService;

    @Mock
    ProcessInstance processInstance;

    @Mock
    Task task;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testStartProcess() {
        JobApplicant applicant = new JobApplicant("Alice", "alice@example.com", "Developer");
        when(runtimeService.startProcessInstanceByKey(eq("handleJobApplicant"), anyMap()))
                .thenReturn(processInstance);
        when(processInstance.getId()).thenReturn("proc123");

        String result = activityService.startProcess(applicant);

        assertEquals("proc123", result);
        verify(runtimeService).startProcessInstanceByKey(eq("handleJobApplicant"), anyMap());
    }

    @Test
    void testCompleteHrReview_TaskExists() {
        when(taskService.createTaskQuery()).thenReturn(mock(org.activiti.engine.task.TaskQuery.class, RETURNS_DEEP_STUBS));
        TaskQueryMockBuilder.queryForGroup(taskService, "hr", "proc1", task);

        activityService.completeHrReview("proc1");

        verify(taskService).complete(task.getId());
    }

    @Test
    void testCompleteEngineeringReview_TaskExists() {
        when(taskService.createTaskQuery()).thenReturn(mock(org.activiti.engine.task.TaskQuery.class, RETURNS_DEEP_STUBS));
        TaskQueryMockBuilder.queryForGroup(taskService, "engineering manager", "proc2", task);

        activityService.completeEngineeringReview("proc2");

        verify(taskService).complete(task.getId());
    }

    @Test
    void testGetApplicant() {
        when(runtimeService.getVariable("proc1", "name")).thenReturn("Bob");
        when(runtimeService.getVariable("proc1", "email")).thenReturn("bob@example.com");
        when(runtimeService.getVariable("proc1", "position")).thenReturn("QA Engineer");

        JobApplicant applicant = activityService.getApplicant("proc1");

        assertEquals("Bob", applicant.getName());
        assertEquals("bob@example.com", applicant.getEmail());
        assertEquals("QA Engineer", applicant.getPosition());
    }

    @Test
    void testGetHrTasks() {
        Task t = mock(Task.class);
        when(t.getId()).thenReturn("t1");
        when(t.getName()).thenReturn("HR Review");
        when(t.getProcessInstanceId()).thenReturn("p1");

        org.activiti.engine.task.TaskQuery tq = mock(org.activiti.engine.task.TaskQuery.class);
        when(tq.taskCandidateGroup("hr")).thenReturn(tq);
        when(tq.list()).thenReturn(List.of(t));
        when(taskService.createTaskQuery()).thenReturn(tq);

        List<TaskDto> result = activityService.getHrTasks();

        assertEquals(1, result.size());
        assertEquals("HR Review", result.get(0).getName());
    }
}
