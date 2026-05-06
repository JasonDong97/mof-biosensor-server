package com.miqroera.biosensor.domain.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.miqroera.biosensor.domain.mapper.FeedbackMapper;
import com.miqroera.biosensor.domain.model.Feedback;
import com.miqroera.biosensor.domain.model.dto.FeedbackCreateDTO;
import com.miqroera.biosensor.domain.model.dto.FeedbackPageParam;
import com.miqroera.biosensor.domain.model.vo.FeedbackVO;
import com.miqroera.biosensor.domain.service.IFeedbackService;
import com.miqroera.biosensor.infra.domain.exception.ServiceException;
import com.miqroera.biosensor.infra.domain.model.PageResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

/**
 * <p>
 * 用户反馈表 服务实现类
 * </p>
 *
 * @author dongjingxiang
 * @since 2026-03-26
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class FeedbackServiceImpl extends ServiceImpl<FeedbackMapper, Feedback> implements IFeedbackService {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Feedback submitFeedback(Long userId, FeedbackCreateDTO dto) {
        Feedback feedback = new Feedback();
        feedback.setUserId(userId);
        feedback.setContent(dto.getContent());
        feedback.setPhoneNumber(dto.getPhoneNumber());
        // 处理图片 URL 列表，转为 JSON 字符串存储
        if (dto.getImages() != null && !dto.getImages().isEmpty()) {
            // 限制最多 3 张图片
            List<String> images = dto.getImages().stream()
                    .limit(3)
                    .filter(StrUtil::isNotBlank)
                    .toList();
            feedback.setImages(JSON.toJSONString(images));
        }

        // 默认状态为待回复
        feedback.setStatus((byte) 0);

        save(feedback);
        log.info("用户提交反馈，userId={}, feedbackId={}", userId, feedback.getId());
        return feedback;
    }

    @Override
    public FeedbackVO getFeedbackDetail(Long userId, Long id) {
        Feedback feedback = getById(id);
        if (feedback == null) {
            throw new ServiceException("反馈记录不存在");
        }
        // 校验是否属于当前用户
        if (!feedback.getUserId().equals(userId)) {
            throw new ServiceException("无权查看该反馈");
        }
        return convertToVO(feedback);
    }

    @Override
    public PageResult<FeedbackVO> getUserFeedbacks(Long userId, FeedbackPageParam param) {
        Page<Feedback> feedbackPage = this.lambdaQuery()
                .eq(StrUtil.isNotBlank(param.getStatus()), Feedback::getStatus, param.getStatus())
                .eq(Objects.nonNull(userId), Feedback::getUserId, userId)
                .page(Page.of(param.getPageNum(), param.getPageSize()));
        return PageResult.build(feedbackPage, FeedbackVO.class);
    }

    /**
     * 将 Feedback 实体转换为 FeedbackVO
     */
    private FeedbackVO convertToVO(Feedback feedback) {
        var vo = BeanUtil.toBean(feedback, FeedbackVO.class);
        List<String> images = null;
        if (StrUtil.isNotBlank(feedback.getImages())) {
            images = JSON.parseArray(feedback.getImages(), String.class);
        }
        vo.setImages(images);
        return vo;
    }
}