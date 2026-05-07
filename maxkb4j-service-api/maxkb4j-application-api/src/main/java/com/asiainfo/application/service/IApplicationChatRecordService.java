package com.asiainfo.application.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.asiainfo.application.entity.ApplicationChatRecordEntity;
import com.asiainfo.application.vo.ApplicationChatRecordVO;

public interface IApplicationChatRecordService extends IService<ApplicationChatRecordEntity> {

    ApplicationChatRecordVO getChatRecordInfo(String chatId, String chatRecordId);

    IPage<ApplicationChatRecordVO> chatRecordPage(String chatId, int current, int size);
}
