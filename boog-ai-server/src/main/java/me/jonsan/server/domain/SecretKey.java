package me.jonsan.server.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import me.jonsan.common.annotation.Excel;
import me.jonsan.common.core.domain.BaseEntity;

/**
 * 密钥对象 boog_ai_secret_key
 * 
 * @author jonsan
 * @date 2025-04-21
 */
public class SecretKey extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long keyId;

    /** 模型id */
    @Excel(name = "模型id")
    private Long modelId;

    /** 密钥名称 */
    @Excel(name = "密钥名称")
    private String keyName;

    /** 密钥值 */
    @Excel(name = "密钥值")
    private String keyValue;

    /** 描述 */
    @Excel(name = "描述")
    private String description;

    /** 状态 */
    @Excel(name = "状态")
    private String status;

    /** 默认 */
    @Excel(name = "默认")
    private String isDefault;

    public void setKeyId(Long keyId) 
    {
        this.keyId = keyId;
    }

    public Long getKeyId() 
    {
        return keyId;
    }

    public void setModelId(Long modelId) 
    {
        this.modelId = modelId;
    }

    public Long getModelId() 
    {
        return modelId;
    }

    public void setKeyName(String keyName) 
    {
        this.keyName = keyName;
    }

    public String getKeyName() 
    {
        return keyName;
    }

    public void setKeyValue(String keyValue) 
    {
        this.keyValue = keyValue;
    }

    public String getKeyValue() 
    {
        return keyValue;
    }

    public void setDescription(String description) 
    {
        this.description = description;
    }

    public String getDescription() 
    {
        return description;
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
            .append("keyId", getKeyId())
            .append("modelId", getModelId())
            .append("keyName", getKeyName())
            .append("keyValue", getKeyValue())
            .append("description", getDescription())
            .append("status", getStatus())
            .append("isDefault", getIsDefault())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
