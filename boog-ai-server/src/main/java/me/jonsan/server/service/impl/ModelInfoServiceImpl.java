package me.jonsan.server.service.impl;

import java.util.List;
import me.jonsan.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import me.jonsan.server.mapper.ModelInfoMapper;
import me.jonsan.server.domain.ModelInfo;
import me.jonsan.server.service.IModelInfoService;

/**
 * 模型Service业务层处理
 * 
 * @author jonsan
 * @date 2025-04-21
 */
@Service
public class ModelInfoServiceImpl implements IModelInfoService 
{
    @Autowired
    private ModelInfoMapper modelInfoMapper;

    /**
     * 查询模型
     * 
     * @param modelId 模型主键
     * @return 模型
     */
    @Override
    public ModelInfo selectModelInfoByModelId(Long modelId)
    {
        return modelInfoMapper.selectModelInfoByModelId(modelId);
    }

    /**
     * 查询模型列表
     * 
     * @param modelInfo 模型
     * @return 模型
     */
    @Override
    public List<ModelInfo> selectModelInfoList(ModelInfo modelInfo)
    {
        return modelInfoMapper.selectModelInfoList(modelInfo);
    }

    /**
     * 新增模型
     * 
     * @param modelInfo 模型
     * @return 结果
     */
    @Override
    public int insertModelInfo(ModelInfo modelInfo)
    {
        modelInfo.setCreateTime(DateUtils.getNowDate());
        return modelInfoMapper.insertModelInfo(modelInfo);
    }

    /**
     * 修改模型
     * 
     * @param modelInfo 模型
     * @return 结果
     */
    @Override
    public int updateModelInfo(ModelInfo modelInfo)
    {
        modelInfo.setUpdateTime(DateUtils.getNowDate());
        return modelInfoMapper.updateModelInfo(modelInfo);
    }

    /**
     * 批量删除模型
     * 
     * @param modelIds 需要删除的模型主键
     * @return 结果
     */
    @Override
    public int deleteModelInfoByModelIds(Long[] modelIds)
    {
        return modelInfoMapper.deleteModelInfoByModelIds(modelIds);
    }

    /**
     * 删除模型信息
     * 
     * @param modelId 模型主键
     * @return 结果
     */
    @Override
    public int deleteModelInfoByModelId(Long modelId)
    {
        return modelInfoMapper.deleteModelInfoByModelId(modelId);
    }
}
