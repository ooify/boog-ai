package me.jonsan.server.service.impl;

import java.util.List;
import me.jonsan.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import me.jonsan.server.mapper.PromptMapper;
import me.jonsan.server.domain.Prompt;
import me.jonsan.server.service.IPromptService;

/**
 * 提示词Service业务层处理
 * 
 * @author jonsan
 * @date 2025-04-21
 */
@Service
public class PromptServiceImpl implements IPromptService 
{
    @Autowired
    private PromptMapper promptMapper;

    /**
     * 查询提示词
     * 
     * @param promptId 提示词主键
     * @return 提示词
     */
    @Override
    public Prompt selectPromptByPromptId(Long promptId)
    {
        return promptMapper.selectPromptByPromptId(promptId);
    }

    /**
     * 查询提示词列表
     * 
     * @param prompt 提示词
     * @return 提示词
     */
    @Override
    public List<Prompt> selectPromptList(Prompt prompt)
    {
        return promptMapper.selectPromptList(prompt);
    }

    /**
     * 新增提示词
     * 
     * @param prompt 提示词
     * @return 结果
     */
    @Override
    public int insertPrompt(Prompt prompt)
    {
        prompt.setCreateTime(DateUtils.getNowDate());
        return promptMapper.insertPrompt(prompt);
    }

    /**
     * 修改提示词
     * 
     * @param prompt 提示词
     * @return 结果
     */
    @Override
    public int updatePrompt(Prompt prompt)
    {
        prompt.setUpdateTime(DateUtils.getNowDate());
        return promptMapper.updatePrompt(prompt);
    }

    /**
     * 批量删除提示词
     * 
     * @param promptIds 需要删除的提示词主键
     * @return 结果
     */
    @Override
    public int deletePromptByPromptIds(Long[] promptIds)
    {
        return promptMapper.deletePromptByPromptIds(promptIds);
    }

    /**
     * 删除提示词信息
     * 
     * @param promptId 提示词主键
     * @return 结果
     */
    @Override
    public int deletePromptByPromptId(Long promptId)
    {
        return promptMapper.deletePromptByPromptId(promptId);
    }
}
