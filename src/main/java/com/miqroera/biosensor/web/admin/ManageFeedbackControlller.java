package com.miqroera.biosensor.web.admin;

import com.miqroera.biosensor.domain.model.dto.FeedbackPageParam;
import com.miqroera.biosensor.domain.model.vo.FeedbackVO;
import com.miqroera.biosensor.domain.service.IFeedbackService;
import com.miqroera.biosensor.infra.domain.model.PageResult;
import com.miqroera.biosensor.infra.domain.model.R;
import com.miqroera.biosensor.infra.util.SecurityUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Tag(name = "用户反馈管理")
@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/admin/feedback")
public class ManageFeedbackControlller {

    private final IFeedbackService feedbackService;

    @Operation(summary = "分页查询用户反馈")
    @GetMapping("/page")
    public R<PageResult<FeedbackVO>> getPage(FeedbackPageParam param){
        SecurityUtils.checkAdmin();
        return R.ok(feedbackService.getUserFeedbacks(null, param));
    }
}
