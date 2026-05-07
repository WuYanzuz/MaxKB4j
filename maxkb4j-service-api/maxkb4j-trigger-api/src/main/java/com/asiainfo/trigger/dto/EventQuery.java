package com.asiainfo.trigger.dto;

import lombok.Data;

@Data
public class EventQuery {
    private String name;
    private String createUser;
    private String type;
    private String task;
    private Boolean isActive;
}
