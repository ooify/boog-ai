package me.jonsan.server.mapper;

import java.util.List;
import me.jonsan.server.domain.Prompt;

/**
 * 提示词Mapper接口
 * 
 * @author jonsan
 * @date 2025-04-21
 */
public interface PromptMapper 
{
    /**
     * 查询提示词
     * 
     * @param promptId 提示词主键
     * @return 提示词
     */
    public Prompt selectPromptByPromptId(Long promptId);

    /**
     * 查询提示词列表
     * 
     * @param prompt 提示词
     * @return 提示词集合
     */
    public List<Prompt> selectPromptList(Prompt prompt);

    /**
     * 新增提示词
     * 
     * @param prompt 提示词
     * @return 结果
     */
    public int insertPrompt(Prompt prompt);

    /**
     * 修改提示词
     * 
     * @param prompt 提示词
     * @return 结果
     */
    public int updatePrompt(Prompt prompt);

    /**
     * 删除提示词
     * 
     * @param promptId 提示词主键
     * @return 结果
     */
    public int deletePromptByPromptId(Long promptId);

    /**
     * 批量删除提示词
     * 
     * @param promptIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deletePromptByPromptIds(Long[] promptIds);
}
