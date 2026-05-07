package com.asiainfo.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.asiainfo.user.dto.UserLoginDTO;
import com.asiainfo.user.entity.UserEntity;
import com.asiainfo.user.vo.UserVO;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;

import java.util.Map;
import java.util.Set;

public interface IUserService extends IService<UserEntity> {
    Set<String> getRoleById(String id);

    Map<String, String> getNicknameMap();

    String getNickname(String userId);

    UserVO getUserById(String userId);

    String login(UserLoginDTO dto, HttpServletRequest request);

    Boolean sendEmailCode(String email, String subject) throws MessagingException;

    boolean checkCode(String email, String code);

}
