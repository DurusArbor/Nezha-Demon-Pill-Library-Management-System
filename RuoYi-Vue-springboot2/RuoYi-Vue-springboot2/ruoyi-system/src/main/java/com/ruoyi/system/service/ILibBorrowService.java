package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.LibBorrow;

/**
 * 图书借阅记录Service接口
 * 
 * @author ruoyi
 * @date 2026-10-19
 */
public interface ILibBorrowService 
{
    /**
     * 查询图书借阅记录
     * 
     * @param borrowId 图书借阅记录主键
     * @return 图书借阅记录
     */
    public LibBorrow selectLibBorrowByBorrowId(Long borrowId);

    /**
     * 查询图书借阅记录列表
     * 
     * @param libBorrow 图书借阅记录
     * @return 图书借阅记录集合
     */
    public List<LibBorrow> selectLibBorrowList(LibBorrow libBorrow);

    /**
     * 新增图书借阅记录
     * 
     * @param libBorrow 图书借阅记录
     * @return 结果
     */
    public int insertLibBorrow(LibBorrow libBorrow);

    /**
     * 修改图书借阅记录
     * 
     * @param libBorrow 图书借阅记录
     * @return 结果
     */
    public int updateLibBorrow(LibBorrow libBorrow);

    /**
     * 批量删除图书借阅记录
     * 
     * @param borrowIds 需要删除的图书借阅记录主键集合
     * @return 结果
     */
    public int deleteLibBorrowByBorrowIds(Long[] borrowIds);

    /**
     * 删除图书借阅记录信息
     *
     * @param borrowId 图书借阅记录主键
     * @return 结果
     */
    public int deleteLibBorrowByBorrowId(Long borrowId);

    /** 借书业务：校验读者/库存，写借阅记录，扣库存 */
    public void doBorrow(Long userId, Long bookId);

    /** 还书业务：回填归还时间，状态置已还，库存回补，触发预约到书提醒 */
    public void doReturn(Long borrowId);

    /** 续借业务：校验状态与续借次数，应还日期顺延 */
    public void renew(Long borrowId);

    /** 查询某读者的借阅列表（含书名、未缴罚金） */
    public List<LibBorrow> selectMyBorrows(Long userId);

    /** 扫描逾期：标记逾期记录并生成罚金，处理预约过期 */
    public void scanOverdue();
}
