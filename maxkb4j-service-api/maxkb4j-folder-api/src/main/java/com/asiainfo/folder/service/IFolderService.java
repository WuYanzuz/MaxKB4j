package com.asiainfo.folder.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.asiainfo.folder.entity.FolderEntity;
import com.asiainfo.folder.vo.FolderVO;

import java.util.List;

public interface IFolderService extends IService<FolderEntity> {

    List<FolderVO> tree(String source);
}
