package com.asiainfo.knowledge.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.asiainfo.common.annotation.SaCheckPerm;
import com.asiainfo.common.constant.AppConst;
import com.asiainfo.common.api.R;
import com.asiainfo.common.enums.PermissionEnum;
import com.asiainfo.knowledge.dto.ProblemDTO;
import com.asiainfo.knowledge.entity.ParagraphEntity;
import com.asiainfo.knowledge.entity.ProblemEntity;
import com.asiainfo.knowledge.service.KnowledgeService;
import com.asiainfo.knowledge.service.ProblemService;
import com.asiainfo.knowledge.vo.ProblemVO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author tarzan
 * @date 2025-03-18 16:00:15
 */
@RestController
@RequestMapping(AppConst.ADMIN_API + "/workspace/default")
@RequiredArgsConstructor
public class ProblemController {

    private final KnowledgeService datasetService;
    private final ProblemService problemService;

    @SaCheckPerm(PermissionEnum.KNOWLEDGE_PROBLEM_CREATE)
    @PostMapping("/knowledge/{id}/problem")
    public R<Boolean> createProblemsByDatasetId(@PathVariable String id, @RequestBody List<String> problems) {
        return R.status(problemService.createProblemsByDatasetId(id, problems));
    }

    @SaCheckPerm(PermissionEnum.KNOWLEDGE_PROBLEM_CREATE)
    @PostMapping("/knowledge/{id}/document/{documentId}/paragraph/{paragraphId}/problem")
    public R<Boolean> createProblemsByParagraphId(@PathVariable String id, @PathVariable String documentId, @PathVariable String paragraphId, @RequestBody ProblemDTO dto) {
        return R.status(problemService.createProblemsByParagraphId(id, documentId, paragraphId, dto));
    }

    @SaCheckPerm(PermissionEnum.KNOWLEDGE_PROBLEM_EDIT)
    @PutMapping("/knowledge/{id}/problem/{problemId}")
    public R<Boolean> updateProblemByDatasetId(@PathVariable String id, @PathVariable String problemId, @RequestBody ProblemEntity problem) {
        problem.setId(problemId);
        return R.status(problemService.updateById(problem));
    }

    @SaCheckPerm(PermissionEnum.KNOWLEDGE_PROBLEM_DELETE)
    @DeleteMapping("/knowledge/{id}/problem/{problemId}")
    public R<Boolean> deleteProblemByDatasetId(@PathVariable("id") String id, @PathVariable("problemId") String problemId) {
        return R.status(problemService.deleteProblemByIds(id, List.of(problemId)));
    }

    @SaCheckPerm(PermissionEnum.KNOWLEDGE_PROBLEM_EDIT)
    @PutMapping("/knowledge/{id}/problem/batch_delete")
    public R<Boolean> deleteBatchProblemByDatasetId(@PathVariable("id") String id, @RequestBody List<String> problemIds) {
        return R.status(problemService.deleteProblemByIds(id, problemIds));
    }

    @SaCheckPerm(PermissionEnum.KNOWLEDGE_PROBLEM_READ)
    @GetMapping("/knowledge/{id}/problem/{page}/{size}")
    public R<IPage<ProblemVO>> getProblemsByDatasetId(@PathVariable String id, @PathVariable("page") int page, @PathVariable("size") int size, String content) {
        return R.data(problemService.pageByDatasetId(id, page, size, content));
    }

    @SaCheckPerm(PermissionEnum.KNOWLEDGE_PROBLEM_READ)
    @GetMapping("/knowledge/{id}/problem/{problemId}/paragraph")
    public R<List<ParagraphEntity>> getParagraphByProblemId(@PathVariable String id, @PathVariable("problemId") String problemId) {
        return R.data(datasetService.getParagraphByProblemId(problemId));
    }


}
