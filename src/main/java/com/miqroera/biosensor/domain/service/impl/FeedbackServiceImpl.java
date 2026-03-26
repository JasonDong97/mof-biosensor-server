package com.miqroera.biosensor.domain.service.impl;

import com.miqroera.biosensor.domain.model.Feedback;
import com.miqroera.biosensor.domain.mapper.FeedbackMapper;
import com.miqroera.biosensor.domain.service.IFeedbackService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户反馈表 服务实现类
 * </p>
 *
 * @author dongjingxiang
 * @since 2026-03-26
 */
@Service
public class FeedbackServiceImpl extends ServiceImpl<FeedbackMapper, Feedback> implements IFeedbackService {

}
