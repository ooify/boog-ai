package me.jonsan.server.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import me.jonsan.common.annotation.Excel;
import me.jonsan.common.core.domain.BaseEntity;

/**
 * 模型对象 boog_ai_model_info
 * 
 * @author jonsan
 * @date 2025-04-21
 */
public class ModelInfo extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long modelId;

    /** 模型代码 */
    @Excel(name = "模型代码")
    private String modelCode;

    /** 模型名称 */
    @Excel(name = "模型名称")
    private String modelName;

    /** 模型版本 */
    @Excel(name = "模型版本")
    private String modelVersion;

    /** 提供方 */
    @Excel(name = "提供方")
    private String provider;

    /** 模型类型 */
    @Excel(name = "模型类型")
    private String modelType;

    /** 模型描述 */
    @Excel(name = "模型描述")
    private String description;

    /** 调用地址 */
    @Excel(name = "调用地址")
    private String apiEndpoint;

    /** 模型特点（0高精准 1低消耗） */
    @Excel(name = "模型特点", readConverterExp = "0=高精准,1=低消耗")
    private String tag;

    /** 状态 */
    @Excel(name = "状态")
    private String status;

    /** 默认 */
    @Excel(name = "默认")
    private String isDefault;

    public void setModelId(Long modelId) 
    {
        this.modelId = modelId;
    }

    public Long getModelId() 
    {
        return modelId;
    }

    public void setModelCode(String modelCode) 
    {
        this.modelCode = modelCode;
    }

    public String getModelCode() 
    {
        return modelCode;
    }

    public void setModelName(String modelName) 
    {
        this.modelName = modelName;
    }

    public String getModelName() 
    {
        return modelName;
    }

    public void setModelVersion(String modelVersion) 
    {
        this.modelVersion = modelVersion;
    }

    public String getModelVersion() 
    {
        return modelVersion;
    }

    public void setProvider(String provider) 
    {
        this.provider = provider;
    }

    public String getProvider() 
    {
        return provider;
    }

    public void setModelType(String modelType) 
    {
        this.modelType = modelType;
    }

    public String getModelType() 
    {
        return modelType;
    }

    public void setDescription(String description) 
    {
        this.description = description;
    }

    public String getDescription() 
    {
        return description;
    }

    public void setApiEndpoint(String apiEndpoint) 
    {
        this.apiEndpoint = apiEndpoint;
    }

    public String getApiEndpoint() 
    {
        return apiEndpoint;
    }

    public void setTag(String tag) 
    {
        this.tag = tag;
    }

    public String getTag() 
    {
        return tag;
    }

    public void setStatus(String status) 
    {
        this.status = status;
    }

    public String getStatus() 
    {
        return status;
    }

    public void setIsDefault(String isDefault) 
    {
        this.isDefault = isDefault;
    }

    public String getIsDefault() 
    {
        return isDefault;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("modelId", getModelId())
            .append("modelCode", getModelCode())
            .append("modelName", getModelName())
            .append("modelVersion", getModelVersion())
            .append("provider", getProvider())
            .append("modelType", getModelType())
            .append("description", getDescription())
            .append("apiEndpoint", getApiEndpoint())
            .append("tag", getTag())
            .append("status", getStatus())
            .append("isDefault", getIsDefault())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
