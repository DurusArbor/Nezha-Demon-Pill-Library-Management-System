package com.ruoyi.system.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 图书借阅记录对象 lib_borrow
 * 
 * @author ruoyi
 * @date 2026-10-19
 */
public class LibBorrow extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 借阅记录ID */
    private Long borrowId;

    /** 读者ID(关联lib_user) */
    @Excel(name = "读者ID(关联lib_user)")
    private Long userId;

    /** 图书ID(关联lib_book) */
    @Excel(name = "图书ID(关联lib_book)")
    private Long bookId;

    /** 借出时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "借出时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date borrowTime;

    /** 归还时间，NULL代表未归还 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "归还时间，NULL代表未归还", width = 30, dateFormat = "yyyy-MM-dd")
    private Date returnTime;

    /** 应还截止日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "应还截止日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date deadlineTime;

    /** 状态 0借阅中 1已归还 2逾期 */
    @Excel(name = "状态 0借阅中 1已归还 2逾期")
    private String borrowStatus;

    /** 已结算魔性的逾期天数（每逾期一天 +5，幂等推进用） */
    private Integer demonSettledDays;

    /** 续借次数 */
    private Integer renewCount;

    /** 图书名称（联查字段） */
    private String bookName;

    /** 读者姓名（联查字段） */
    private String userName;

    /** 未缴罚金金额（联查字段） */
    private Double fineAmount;

    public void setBorrowId(Long borrowId) 
    {
        this.borrowId = borrowId;
    }

    public Long getBorrowId() 
    {
        return borrowId;
    }

    public void setUserId(Long userId) 
    {
        this.userId = userId;
    }

    public Long getUserId() 
    {
        return userId;
    }

    public void setBookId(Long bookId) 
    {
        this.bookId = bookId;
    }

    public Long getBookId() 
    {
        return bookId;
    }

    public void setBorrowTime(Date borrowTime) 
    {
        this.borrowTime = borrowTime;
    }

    public Date getBorrowTime() 
    {
        return borrowTime;
    }

    public void setReturnTime(Date returnTime) 
    {
        this.returnTime = returnTime;
    }

    public Date getReturnTime() 
    {
        return returnTime;
    }

    public void setDeadlineTime(Date deadlineTime) 
    {
        this.deadlineTime = deadlineTime;
    }

    public Date getDeadlineTime() 
    {
        return deadlineTime;
    }

    public void setDemonSettledDays(Integer demonSettledDays) 
    {
        this.demonSettledDays = demonSettledDays;
    }

    public Integer getDemonSettledDays() 
    {
        return demonSettledDays;
    }

    public void setBorrowStatus(String borrowStatus) 
    {
        this.borrowStatus = borrowStatus;
    }

    public String getBorrowStatus() 
    {
        return borrowStatus;
    }

    public void setRenewCount(Integer renewCount) 
    {
        this.renewCount = renewCount;
    }

    public Integer getRenewCount() 
    {
        return renewCount;
    }

    public void setBookName(String bookName) 
    {
        this.bookName = bookName;
    }

    public String getBookName() 
    {
        return bookName;
    }

    public void setUserName(String userName) 
    {
        this.userName = userName;
    }

    public String getUserName() 
    {
        return userName;
    }

    public void setFineAmount(Double fineAmount) 
    {
        this.fineAmount = fineAmount;
    }

    public Double getFineAmount() 
    {
        return fineAmount;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("borrowId", getBorrowId())
            .append("userId", getUserId())
            .append("bookId", getBookId())
            .append("borrowTime", getBorrowTime())
            .append("returnTime", getReturnTime())
            .append("deadlineTime", getDeadlineTime())
            .append("borrowStatus", getBorrowStatus())
            .append("renewCount", getRenewCount())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
