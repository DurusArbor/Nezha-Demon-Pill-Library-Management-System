package com.ruoyi.system.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 图书分类对象 lib_category
 *
 * @author ruoyi
 */
public class LibCategory extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 分类ID */
    private Long categoryId;

    /** 分类名称 */
    @Excel(name = "分类名称")
    private String categoryName;

    /** 描述 */
    @Excel(name = "描述")
    private String description;

    /** 排序 */
    private Integer sortNum;

    public void setCategoryId(Long categoryId) { this.categoryId = categoryId; }
    public Long getCategoryId() { return categoryId; }
    public void setCategoryName(String categoryName) { this.categoryName = categoryName; }
    public String getCategoryName() { return categoryName; }
    public void setDescription(String description) { this.description = description; }
    public String getDescription() { return description; }
    public void setSortNum(Integer sortNum) { this.sortNum = sortNum; }
    public Integer getSortNum() { return sortNum; }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
            .append("categoryId", getCategoryId())
            .append("categoryName", getCategoryName())
            .append("description", getDescription())
            .append("sortNum", getSortNum())
            .append("createTime", getCreateTime())
            .toString();
    }
}
