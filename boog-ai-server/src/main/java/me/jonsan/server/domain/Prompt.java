package me.jonsan.server.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import me.jonsan.common.annotation.Excel;
import me.jonsan.common.core.domain.BaseEntity;

/**
 * 提示词对象 boog_ai_prompt
 * 
 * @author jonsan
 * @date 2025-04-21
 */
public class Prompt extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long promptId;

    /** 提示词名称 */
    @Excel(name = "提示词名称")
    private String promptName;

    /** 提示词内容 */
    @Excel(name = "提示词内容")
    private String promptText;

    /** 描述 */
    @Excel(name = "描述")
    private String description;

    /** 模型id */
    @Excel(name = "模型id")
    private Long modelId;

    /** 提示词类型 */
    @Excel(name = "提示词类型")
    private String tag;

    /** 状态 */
    @Excel(name = "状态")
    private String status;

    /** 默认 */
    @Excel(name = "默认")
    private String isDefault;

    public void setPromptId(Long promptId) 
    {
        this.promptId = promptId;
    }

    public Long getPromptId() 
    {
        return promptId;
    }

    public void setPromptName(String promptName) 
    {
        this.promptName = promptName;
    }

    public String getPromptName() 
    {
        return promptName;
    }

    public void setPromptText(String promptText) 
    {
        this.promptText = promptText;
    }

    public String getPromptText() 
    {
        return promptText;
    }

    public void setDescription(String description) 
    {
        this.description = description;
    }

    public String getDescription() 
    {
        return description;
    }

    public void setModelId(Long modelId) 
    {
        this.modelId = modelId;
    }

    public Long getModelId() 
    {
        return modelId;
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
            .append("promptId", getPromptId())
            .append("promptName", getPromptName())
            .append("promptText", getPromptText())
            .append("description", getDescription())
            .append("modelId", getModelId())
            .append("tag", getTag())
            .append("status", getStatus())
            .append("isDefault", getIsDefault())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
