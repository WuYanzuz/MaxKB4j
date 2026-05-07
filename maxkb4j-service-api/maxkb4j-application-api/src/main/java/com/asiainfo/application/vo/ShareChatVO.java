package com.asiainfo.application.vo;

import com.asiainfo.application.entity.ApplicationChatRecordEntity;
import lombok.Data;

import java.util.List;

@Data
public class ShareChatVO {

    private String summary;
    private List<ApplicationChatRecordEntity> chatRecordList;
}
