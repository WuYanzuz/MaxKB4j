package com.asiainfo.user.dto;

import lombok.Data;
import lombok.NonNull;

@Data
public class UserLoginDTO {
    @NonNull
    private String username;
    @NonNull
    private String password;
    private String captcha;
}
