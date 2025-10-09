package org.jobapplicant.control;

import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;

import static org.mockito.Mockito.*;

class TaskQueryMockBuilder {
    static void queryForGroup(TaskService taskService, String group, String processId, Task task) {
        TaskQuery query = mock(TaskQuery.class);
        when(taskService.createTaskQuery()).thenReturn(query);
        when(query.processInstanceId(processId)).thenReturn(query);
        when(query.taskCandidateGroup(group)).thenReturn(query);
        when(query.singleResult()).thenReturn(task);
        when(task.getId()).thenReturn("t1");
    }
}
