package com.miqroera.biosensor.domain.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.miqroera.biosensor.domain.model.Feedback;
import com.miqroera.biosensor.domain.model.dto.FeedbackCreateDTO;
import com.miqroera.biosensor.domain.model.vo.FeedbackVO;

/**
 * <p>
 * 用户反馈表 服务类
 * </p>
 *
 * @author dongjingxiang
 * @since 2026-03-26
 */
public interface IFeedbackService extends IService<Feedback> {

    /**
     * 提交用户反馈
     *
     * @param userId 用户 ID
     * @param dto    反馈提交参数
     * @return 反馈记录
     */
    Feedback submitFeedback(Long userId, FeedbackCreateDTO dto);

    /**
     * 分页查询用户反馈列表
     *
     * @param userId 用户 ID
     * @param current 当前页
     * @param size    每页大小
     * @return 反馈列表分页结果
     */
    Page<FeedbackVO> getUserFeedbacks(Long userId, Integer current, Integer size);

    /**
     * 根据 ID 获取反馈详情
     *
     * @param userId 用户 ID
     * @param id     反馈 ID
     * @return 反馈详情
     */
    FeedbackVO getFeedbackDetail(Long userId, Long id);
}
