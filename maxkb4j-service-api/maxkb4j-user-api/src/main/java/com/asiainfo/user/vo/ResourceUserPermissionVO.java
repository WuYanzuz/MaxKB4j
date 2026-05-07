package com.asiainfo.user.vo;

import com.asiainfo.user.entity.UserResourcePermissionEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class ResourceUserPermissionVO extends UserResourcePermissionEntity {

    private String id;
    private String userId;
    private String username;
    private String nickname;
    private String permission;
}
