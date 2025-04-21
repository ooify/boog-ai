package me.jonsan.server.mapper;

import java.util.List;
import me.jonsan.server.domain.ModelInfo;

/**
 * 模型Mapper接口
 * 
 * @author jonsan
 * @date 2025-04-21
 */
public interface ModelInfoMapper 
{
    /**
     * 查询模型
     * 
     * @param modelId 模型主键
     * @return 模型
     */
    public ModelInfo selectModelInfoByModelId(Long modelId);

    /**
     * 查询模型列表
     * 
     * @param modelInfo 模型
     * @return 模型集合
     */
    public List<ModelInfo> selectModelInfoList(ModelInfo modelInfo);

    /**
     * 新增模型
     * 
     * @param modelInfo 模型
     * @return 结果
     */
    public int insertModelInfo(ModelInfo modelInfo);

    /**
     * 修改模型
     * 
     * @param modelInfo 模型
     * @return 结果
     */
    public int updateModelInfo(ModelInfo modelInfo);

    /**
     * 删除模型
     * 
     * @param modelId 模型主键
     * @return 结果
     */
    public int deleteModelInfoByModelId(Long modelId);

    /**
     * 批量删除模型
     * 
     * @param modelIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteModelInfoByModelIds(Long[] modelIds);
}
