package com.asiainfo.knowledge.controller;

import com.asiainfo.common.annotation.SaCheckPerm;
import com.asiainfo.common.constant.AppConst;
import com.asiainfo.common.enums.PermissionEnum;
import com.asiainfo.knowledge.service.TemplateService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author tarzan
 * @date 2024-12-25 16:00:15
 */
@RestController
@RequestMapping(AppConst.ADMIN_API + "/workspace")
@RequiredArgsConstructor
public class DocumentTemplateController {

    private final TemplateService templateService;

    @SaCheckPerm(PermissionEnum.KNOWLEDGE_DOCUMENT_EXPORT)
    @GetMapping("/knowledge/document/table_template/export")
    public void tableTemplateExport(String type, HttpServletResponse response) throws Exception {
        templateService.tableTemplateExport(type, response);
    }
    @SaCheckPerm(PermissionEnum.KNOWLEDGE_DOCUMENT_EXPORT)
    @GetMapping("/knowledge/document/template/export")
    public void templateExport(String type, HttpServletResponse response) throws Exception {
        templateService.qaTemplateExport(type, response);
    }
}
