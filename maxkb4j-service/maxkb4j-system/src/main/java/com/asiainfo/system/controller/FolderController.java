package com.asiainfo.system.controller;

import com.asiainfo.common.constant.AppConst;
import com.asiainfo.common.api.R;
import com.asiainfo.folder.service.IFolderService;
import com.asiainfo.folder.vo.FolderVO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(AppConst.ADMIN_API + "/workspace/default")
@RequiredArgsConstructor
public class FolderController {

    private final IFolderService folderService;

    @GetMapping("/{source}/folder")
    public R<List<FolderVO>> folderTree(@PathVariable String source) {
        return R.data(folderService.tree(source));
    }


}
