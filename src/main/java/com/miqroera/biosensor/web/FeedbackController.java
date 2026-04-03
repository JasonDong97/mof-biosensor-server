package com.miqroera.biosensor.web;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import com.miqroera.biosensor.domain.model.dto.FeedbackCreateDTO;
import com.miqroera.biosensor.domain.model.vo.FeedbackVO;
import com.miqroera.biosensor.domain.service.IFeedbackService;
import com.miqroera.biosensor.infra.domain.model.R;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * 用户反馈控制器
 *
 * @author dongjingxiang
 * @since 2026-04-03
 */
@Slf4j
@Tag(name = "用户反馈", description = "反馈提交、查询")
@ApiSupport(order = 3)
@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/feedbacks")
public class FeedbackController {

    private final IFeedbackService feedbackService;

    /**
     * 提交用户反馈
     */
    @PostMapping
    @Operation(summary = "提交用户反馈", description = "用户提交反馈内容，支持文字和图片")
    public R<Void> submitFeedback(@Valid @RequestBody FeedbackCreateDTO dto) {
        Long userId = StpUtil.getLoginIdAsLong();
        feedbackService.submitFeedback(userId, dto);
        return R.ok();
    }

    /**
     * 获取用户反馈列表
     */
    @GetMapping
    @Operation(summary = "获取用户反馈列表", description = "分页获取当前用户的反馈列表")
    public R<Page<FeedbackVO>> getFeedbackList(
            @RequestParam(defaultValue = "1") Integer current,
            @RequestParam(defaultValue = "10") Integer size) {
        Long userId = StpUtil.getLoginIdAsLong();
        Page<FeedbackVO> page = feedbackService.getUserFeedbacks(userId, current, size);
        return R.ok(page);
    }

    /**
     * 获取反馈详情
     */
    @GetMapping("/{id}")
    @Operation(summary = "获取反馈详情", description = "根据 ID 获取单条反馈详情")
    public R<FeedbackVO> getFeedbackDetail(@PathVariable Long id) {
        Long userId = StpUtil.getLoginIdAsLong();
        FeedbackVO feedbackVO = feedbackService.getFeedbackDetail(userId, id);
        return R.ok(feedbackVO);
    }
}