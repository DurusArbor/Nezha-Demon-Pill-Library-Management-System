package com.ruoyi.system.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 图书信息对象 lib_book
 * 
 * @author ruoyi
 * @date 2026-10-19
 */
public class LibBook extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 图书ID */
    private Long bookId;

    /** 图书名称 */
    @Excel(name = "图书名称")
    private String bookName;

    /** 作者 */
    @Excel(name = "作者")
    private String bookAuthor;

    /** ISBN编号 */
    @Excel(name = "ISBN编号")
    private String bookIsbn;

    /** 出版社 */
    @Excel(name = "出版社")
    private String bookPublisher;

    /** 出版日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "出版日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date publishDate;

    /** 馆藏总数量 */
    @Excel(name = "馆藏总数量")
    private Long bookCount;

    /** 已借出数量 */
    @Excel(name = "已借出数量")
    private Long borrowCount;

    /** 状态：0正常，1下架 */
    @Excel(name = "状态：0正常，1下架")
    private String bookStatus;

    /** 分类ID */
    private Long categoryId;

    /** 分类名称（联表查询） */
    @Excel(name = "分类")
    private String categoryName;

    /** 可借数量（联查字段：book_count - borrow_count） */
    private Integer availableCount;

    public void setBookId(Long bookId) 
    {
        this.bookId = bookId;
    }

    public Long getBookId() 
    {
        return bookId;
    }

    public void setBookName(String bookName) 
    {
        this.bookName = bookName;
    }

    public String getBookName() 
    {
        return bookName;
    }

    public void setBookAuthor(String bookAuthor) 
    {
        this.bookAuthor = bookAuthor;
    }

    public String getBookAuthor() 
    {
        return bookAuthor;
    }

    public void setBookIsbn(String bookIsbn) 
    {
        this.bookIsbn = bookIsbn;
    }

    public String getBookIsbn() 
    {
        return bookIsbn;
    }

    public void setBookPublisher(String bookPublisher) 
    {
        this.bookPublisher = bookPublisher;
    }

    public String getBookPublisher() 
    {
        return bookPublisher;
    }

    public void setPublishDate(Date publishDate) 
    {
        this.publishDate = publishDate;
    }

    public Date getPublishDate() 
    {
        return publishDate;
    }

    public void setBookCount(Long bookCount) 
    {
        this.bookCount = bookCount;
    }

    public Long getBookCount() 
    {
        return bookCount;
    }

    public void setBorrowCount(Long borrowCount) 
    {
        this.borrowCount = borrowCount;
    }

    public Long getBorrowCount() 
    {
        return borrowCount;
    }

    public void setBookStatus(String bookStatus) 
    {
        this.bookStatus = bookStatus;
    }

    public String getBookStatus() 
    {
        return bookStatus;
    }

    public void setCategoryId(Long categoryId) { this.categoryId = categoryId; }
    public Long getCategoryId() { return categoryId; }
    public void setCategoryName(String categoryName) { this.categoryName = categoryName; }
    public String getCategoryName() { return categoryName; }
    public void setAvailableCount(Integer availableCount) { this.availableCount = availableCount; }
    public Integer getAvailableCount() { return availableCount; }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("bookId", getBookId())
            .append("bookName", getBookName())
            .append("bookAuthor", getBookAuthor())
            .append("bookIsbn", getBookIsbn())
            .append("bookPublisher", getBookPublisher())
            .append("publishDate", getPublishDate())
            .append("bookCount", getBookCount())
            .append("borrowCount", getBorrowCount())
            .append("bookStatus", getBookStatus())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
