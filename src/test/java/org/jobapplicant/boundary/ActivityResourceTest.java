package org.jobapplicant.boundary;

import org.jobapplicant.control.ActivityService;
import org.jobapplicant.entity.JobApplicant;
import org.jobapplicant.entity.TaskDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import jakarta.ws.rs.core.Response;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ActivityResourceTest {

    @InjectMocks
    ActivityResource activityResource;

    @Mock
    ActivityService activityService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testStartApplication_Success() {
        JobApplicant applicant = new JobApplicant("Alice", "alice@example.com", "Engineer");
        when(activityService.startProcess(applicant)).thenReturn("proc123");

        Response response = activityResource.startApplication(applicant);

        assertEquals(200, response.getStatus());
        Map<?, ?> body = (Map<?, ?>) response.getEntity();
        assertEquals("proc123", body.get("processId"));
    }

    @Test
    void testStartApplication_Error() {
        JobApplicant applicant = new JobApplicant("Alice", "alice@example.com", "Engineer");
        when(activityService.startProcess(applicant)).thenThrow(new RuntimeException("Invalid data"));

        Response response = activityResource.startApplication(applicant);

        assertEquals(400, response.getStatus());
        Map<?, ?> body = (Map<?, ?>) response.getEntity();
        assertEquals("Invalid data", body.get("error"));
    }

    @Test
    void testCompleteHrReview() {
        Map<String, String> result = activityResource.completeHrReview("proc1");

        verify(activityService).completeHrReview("proc1");
        assertEquals("HR Review completed", result.get("message"));
    }

    @Test
    void testCompleteEngineeringReview() {
        Map<String, String> result = activityResource.completeEngineeringReview("proc1");

        verify(activityService).completeEngineeringReview("proc1");
        assertEquals("Engineering Review completed and Process ended", result.get("message"));
    }

    @Test
    void testGetApplicant() {
        JobApplicant applicant = new JobApplicant("Bob", "bob@example.com", "QA");
        when(activityService.getApplicant("proc1")).thenReturn(applicant);

        JobApplicant result = activityResource.getApplicant("proc1");

        verify(activityService).getApplicant("proc1");
        assertEquals("Bob", result.getName());
    }

    @Test
    void testGetHrTasks() {
        List<TaskDto> mockList = List.of(new TaskDto("t1", "HR Review", "p1"));
        when(activityService.getHrTasks()).thenReturn(mockList);

        List<TaskDto> result = activityResource.getHrTasks();

        assertEquals(1, result.size());
        verify(activityService).getHrTasks();
    }

    @Test
    void testGetEngineeringTasks() {
        List<TaskDto> mockList = List.of(new TaskDto("t2", "Eng Review", "p2"));
        when(activityService.getEngineeringTasks()).thenReturn(mockList);

        List<TaskDto> result = activityResource.getEngineeringTasks();

        assertEquals(1, result.size());
        verify(activityService).getEngineeringTasks();
    }
}
