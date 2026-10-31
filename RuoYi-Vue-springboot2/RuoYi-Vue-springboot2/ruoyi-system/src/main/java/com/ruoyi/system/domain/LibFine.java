package com.ruoyi.system.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 借阅罚金对象 lib_fine
 * 
 * @author ruoyi
 */
public class LibFine extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 罚金ID */
    private Long fineId;

    /** 借阅记录ID(关联lib_borrow) */
    @Excel(name = "借阅记录ID")
    private Long borrowId;

    /** 读者ID(关联lib_user) */
    @Excel(name = "读者ID")
    private Long userId;

    /** 图书ID(关联lib_book) */
    @Excel(name = "图书ID")
    private Long bookId;

    /** 逾期天数 */
    @Excel(name = "逾期天数")
    private Integer overdueDays;

    /** 罚金金额 */
    @Excel(name = "罚金金额")
    private Double fineAmount;

    /** 状态 0未缴 1已缴 */
    @Excel(name = "状态 0未缴 1已缴")
    private String fineStatus;

    /** 缴纳时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "缴纳时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date payTime;

    /** 图书名称（联查字段） */
    private String bookName;

    /** 读者姓名（联查字段） */
    private String userName;

    /** 借阅状态（联查字段）：0借阅中 1已归还 2逾期 */
    private String borrowStatus;

    public void setFineId(Long fineId) 
    {
        this.fineId = fineId;
    }

    public Long getFineId() 
    {
        return fineId;
    }

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

    public void setOverdueDays(Integer overdueDays) 
    {
        this.overdueDays = overdueDays;
    }

    public Integer getOverdueDays() 
    {
        return overdueDays;
    }

    public void setFineAmount(Double fineAmount) 
    {
        this.fineAmount = fineAmount;
    }

    public Double getFineAmount() 
    {
        return fineAmount;
    }

    public void setFineStatus(String fineStatus) 
    {
        this.fineStatus = fineStatus;
    }

    public String getFineStatus() 
    {
        return fineStatus;
    }

    public void setPayTime(Date payTime) 
    {
        this.payTime = payTime;
    }

    public Date getPayTime() 
    {
        return payTime;
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

    public void setBorrowStatus(String borrowStatus)
    {
        this.borrowStatus = borrowStatus;
    }

    public String getBorrowStatus()
    {
        return borrowStatus;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("fineId", getFineId())
            .append("borrowId", getBorrowId())
            .append("userId", getUserId())
            .append("bookId", getBookId())
            .append("overdueDays", getOverdueDays())
            .append("fineAmount", getFineAmount())
            .append("fineStatus", getFineStatus())
            .append("payTime", getPayTime())
            .append("createTime", getCreateTime())
            .append("remark", getRemark())
            .toString();
    }
}
