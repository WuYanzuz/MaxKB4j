package com.asiainfo.user.vo;

import com.asiainfo.user.entity.UserResourcePermissionEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class UserResourcePermissionVO extends UserResourcePermissionEntity {

    private String name;
    private String icon;
    private String folderId;
    private String permission;

}
