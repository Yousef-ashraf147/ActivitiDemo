package org.jobapplicant.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class TaskDto {
    private String id;
    private String name;
    private String processInstanceId;

}
