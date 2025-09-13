package org.jobapplicant.control;


import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import org.activiti.engine.*;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.jobapplicant.entity.JobApplicant;
import org.jobapplicant.entity.TaskDto;

import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.stream.Collectors;


@ApplicationScoped
    public class ActivityService {

        private ProcessEngine processEngine;
        private RuntimeService runtimeService;
        private TaskService taskService;
        private RepositoryService repositoryService;

        @PostConstruct
        public void init() {
            ProcessEngineConfiguration cfg = ProcessEngineConfiguration
                    .createStandaloneInMemProcessEngineConfiguration()
                    .setJdbcUrl("jdbc:h2:mem:activiti;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE")
                    .setJdbcDriver("org.h2.Driver")
                    .setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE);

            processEngine = cfg.buildProcessEngine();
            runtimeService = processEngine.getRuntimeService();
            taskService = processEngine.getTaskService();
            repositoryService = processEngine.getRepositoryService();


            repositoryService.createDeployment()
                    .addClasspathResource("handle-job-applicant.bpmn20.xml")
                    .deploy();
        }

    public String startProcess(JobApplicant applicant) {

        Map<String, Object> variables = new HashMap<>();
        variables.put("name", applicant.getName());
        variables.put("email", applicant.getEmail());
        variables.put("position", applicant.getPosition());

        ProcessInstance processInstance =
                runtimeService.startProcessInstanceByKey("handleJobApplicant", variables);
//        List<Task> tasks = taskService.createTaskQuery().list();
//        System.out.println("All tasks in engine: " + tasks.size());
//        tasks.forEach(t -> {
//            System.out.println("Task: " + t.getName());
//            taskService.getIdentityLinksForTask(t.getId())
//                    .forEach(link ->
//                            System.out.println("   type=" + link.getType()
//                                    + " group=" + link.getGroupId()));}
//        );
        return processInstance.getId();
    }

    public void completeHrReview(String processInstanceId) {
        Task task = taskService.createTaskQuery()
                .processInstanceId(processInstanceId)
                .taskCandidateGroup("hr")
                .singleResult();

        if (task != null) {
            taskService.complete(task.getId());
//            List<TaskDto> engineeringTasks = getEngineeringTasks();
//
//            System.out.println("Engineering tasks: " + engineeringTasks.size());
//            engineeringTasks.forEach(t ->
//                    System.out.println("New task: " + t.getName() + " in process " + t.getProcessInstanceId()));
        }
    }

    public void completeEngineeringReview(String processInstanceId) {
        Task task = taskService.createTaskQuery()
                .processInstanceId(processInstanceId)
                .taskCandidateGroup("engineering manager")
                .singleResult();

        if (task != null) {
            taskService.complete(task.getId());
        }
    }

    public JobApplicant getApplicant(String processInstanceId) {

        String name = (String) runtimeService.getVariable(processInstanceId, "name");
        String email = (String) runtimeService.getVariable(processInstanceId, "email");
        String position = (String) runtimeService.getVariable(processInstanceId, "position");


        return new JobApplicant(name, email, position);
    }

    public List<TaskDto> getHrTasks() {
        return taskService.createTaskQuery()
                .taskCandidateGroup("hr")
                .list()
                .stream()
                .map(t -> new TaskDto(t.getId(), t.getName(), t.getProcessInstanceId()))
                .collect(Collectors.toList());
    }

    public List<TaskDto> getEngineeringTasks() {
        return taskService.createTaskQuery()
                .taskCandidateGroup("engineering manager")
                .list()
                .stream()
                .map(t -> new TaskDto(t.getId(), t.getName(), t.getProcessInstanceId()))
                .collect(Collectors.toList());
    }

    }

