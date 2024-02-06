package org.antontech.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectDTO {
    private long projectId;
    private Date startDate;
    private String description;
    private String manager;
    private List<Long> userIds;
}
