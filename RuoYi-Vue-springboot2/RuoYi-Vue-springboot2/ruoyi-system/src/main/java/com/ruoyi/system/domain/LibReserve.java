package com.ruoyi.system.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 图书预约对象 lib_reserve
 * 
 * @author ruoyi
 */
public class LibReserve extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 预约ID */
    private Long reserveId;

    /** 读者ID(关联lib_user) */
    @Excel(name = "读者ID")
    private Long userId;

    /** 图书ID(关联lib_book) */
    @Excel(name = "图书ID")
    private Long bookId;

    /** 预约时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "预约时间", width = 20, dateFormat = "yyyy-MM-dd")
    private Date reserveTime;

    /** 到书保留截止时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "到书保留截止时间", width = 20, dateFormat = "yyyy-MM-dd")
    private Date expireTime;

    /** 状态 0预约中 1已到书待借 2已借出 3已取消 4已过期 */
    @Excel(name = "状态 0预约中 1已到书待借 2已借出 3已取消 4已过期")
    private String reserveStatus;

    /** 图书名称（联查字段） */
    private String bookName;

    /** 读者姓名（联查字段） */
    private String userName;

    public void setReserveId(Long reserveId) 
    {
        this.reserveId = reserveId;
    }

    public Long getReserveId() 
    {
        return reserveId;
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

    public void setReserveTime(Date reserveTime) 
    {
        this.reserveTime = reserveTime;
    }

    public Date getReserveTime() 
    {
        return reserveTime;
    }

    public void setExpireTime(Date expireTime) 
    {
        this.expireTime = expireTime;
    }

    public Date getExpireTime() 
    {
        return expireTime;
    }

    public void setReserveStatus(String reserveStatus) 
    {
        this.reserveStatus = reserveStatus;
    }

    public String getReserveStatus() 
    {
        return reserveStatus;
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

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("reserveId", getReserveId())
            .append("userId", getUserId())
            .append("bookId", getBookId())
            .append("reserveTime", getReserveTime())
            .append("expireTime", getExpireTime())
            .append("reserveStatus", getReserveStatus())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
