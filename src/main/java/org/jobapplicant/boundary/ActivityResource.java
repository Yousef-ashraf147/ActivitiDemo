package org.jobapplicant.boundary;


import org.activiti.engine.task.Task;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.jobapplicant.control.ActivityService;
import org.jobapplicant.entity.JobApplicant;
import org.jobapplicant.entity.TaskDto;

import java.util.List;
import java.util.Map;

import java.util.List;

@Path("/api/job-applicants")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Job Applicant Process", description = "BPMN workflow for handling job applications")
public class ActivityResource {

    @Inject
    ActivityService activityService;

    @POST
    @Path("/start")
    @Operation(summary = "Start job application process", description = "Starts a new BPMN process instance for a job applicant")
    @APIResponse(responseCode = "200", description = "Process started successfully")
    @APIResponse(responseCode = "400", description = "Invalid input data")
    public Response startApplication(@Valid JobApplicant applicant) {
        try {
            String processId = activityService.startProcess(applicant);
            return Response.ok(Map.of("processId", processId)).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Map.of("error", e.getMessage())).build();
        }
    }

    @POST
    @Path("/{processId}/hr-complete")
    @Operation(summary = "Complete HR Review", description = "Completes the HR review task in the workflow")
    @APIResponse(responseCode = "200", description = "HR review completed")
    public Map<String, String> completeHrReview(@PathParam("processId") String processId) {
        activityService.completeHrReview(processId);
        return Map.of("message", "HR Review completed");
    }

    @POST
    @Path("/{processId}/engineering-complete")
    @Operation(summary = "Complete Engineering Review", description = "Completes the engineering manager review task")
    @APIResponse(responseCode = "200", description = "Engineering review completed")
    public Map<String, String> completeEngineeringReview(@PathParam("processId") String processId) {
        activityService.completeEngineeringReview(processId);
        return Map.of("message", "Engineering Review completed and Process ended");
    }

    @GET
    @Path("/{processId}")
    @Operation(summary = "Get applicant details", description = "Retrieves job applicant information by process ID")
    @APIResponse(responseCode = "200", description = "Applicant found")
    @APIResponse(responseCode = "404", description = "Process not found")
    public JobApplicant getApplicant(@PathParam("processId") String processId) {
        return activityService.getApplicant(processId);
    }

    @GET
    @Path("/tasks/hr")
    @Operation(summary = "Get HR tasks", description = "Retrieves all pending HR review tasks")
    @APIResponse(responseCode = "200", description = "List of HR tasks")
    public List<TaskDto> getHrTasks() {
        return activityService.getHrTasks();
    }

    @GET
    @Path("/tasks/engineering")
    @Operation(summary = "Get Engineering tasks", description = "Retrieves all pending engineering review tasks")
    @APIResponse(responseCode = "200", description = "List of engineering tasks")
    public List<TaskDto> getEngineeringTasks() {
        return activityService.getEngineeringTasks();
    }


}
