package com.ruoyi.system.domain;

import com.ruoyi.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 逾期催缴记录对象 lib_remind
 *
 * @author ruoyi
 */
public class LibRemind extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 催缴ID */
    private Long remindId;

    /** 借阅记录ID */
    private Long borrowId;

    /** 读者ID */
    private Long userId;

    /** 图书ID */
    private Long bookId;

    /** 催缴内容 */
    private String remindContent;

    /** 状态 0未读 1已读 */
    private String status;

    /** 图书名称（联查字段） */
    private String bookName;

    /** 读者姓名（联查字段） */
    private String userName;

    public Long getRemindId()
    {
        return remindId;
    }

    public void setRemindId(Long remindId)
    {
        this.remindId = remindId;
    }

    public Long getBorrowId()
    {
        return borrowId;
    }

    public void setBorrowId(Long borrowId)
    {
        this.borrowId = borrowId;
    }

    public Long getUserId()
    {
        return userId;
    }

    public void setUserId(Long userId)
    {
        this.userId = userId;
    }

    public Long getBookId()
    {
        return bookId;
    }

    public void setBookId(Long bookId)
    {
        this.bookId = bookId;
    }

    public String getRemindContent()
    {
        return remindContent;
    }

    public void setRemindContent(String remindContent)
    {
        this.remindContent = remindContent;
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    public String getBookName()
    {
        return bookName;
    }

    public void setBookName(String bookName)
    {
        this.bookName = bookName;
    }

    public String getUserName()
    {
        return userName;
    }

    public void setUserName(String userName)
    {
        this.userName = userName;
    }

    @Override
    public String toString()
    {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("remindId", getRemindId())
                .append("borrowId", getBorrowId())
                .append("userId", getUserId())
                .append("bookId", getBookId())
                .append("remindContent", getRemindContent())
                .append("status", getStatus())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("remark", getRemark())
                .toString();
    }
}
