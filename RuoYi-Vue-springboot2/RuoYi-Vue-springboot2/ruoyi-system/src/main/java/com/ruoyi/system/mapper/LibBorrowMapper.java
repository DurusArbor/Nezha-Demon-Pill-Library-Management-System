package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.LibBorrow;

/**
 * 图书借阅记录Mapper接口
 * 
 * @author ruoyi
 * @date 2026-10-19
 */
public interface LibBorrowMapper 
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
     * 删除图书借阅记录
     * 
     * @param borrowId 图书借阅记录主键
     * @return 结果
     */
    public int deleteLibBorrowByBorrowId(Long borrowId);

    /**
     * 批量删除图书借阅记录
     *
     * @param borrowIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteLibBorrowByBorrowIds(Long[] borrowIds);

    /** 统计某读者当前未归还的借阅数 */
    public int countActiveByUserId(Long userId);

    /** 统计某读者当前逾期未还的数量 */
    public int countOverdueByUserId(Long userId);

    /** 统计某读者累计借阅总次数（含已还） */
    public int countTotalByUserId(Long userId);

    /** 统计某读者按时归还次数（归还时间不晚于应还日期） */
    public int countOnTimeReturnByUserId(Long userId);

    /** 检查某读者是否已借某书未还 */
    public LibBorrow selectActiveByUserAndBook(@org.apache.ibatis.annotations.Param("userId") Long userId,
                                               @org.apache.ibatis.annotations.Param("bookId") Long bookId);

    /** 自动把超过截止日期仍未归还的记录标记为逾期 */
    public int markOverdueRecords();

    /** 更新某借阅已结算魔性的逾期天数 */
    public int updateDemonSettledDays(@org.apache.ibatis.annotations.Param("borrowId") Long borrowId,
                                      @org.apache.ibatis.annotations.Param("days") Integer days);

    /** 续借：续借次数+1，应还日期顺延 */
    public int updateRenew(@org.apache.ibatis.annotations.Param("borrowId") Long borrowId,
                           @org.apache.ibatis.annotations.Param("days") Integer days);

    /** 查询某读者的借阅列表（含书名、读者名、未缴罚金） */
    public List<LibBorrow> selectMyBorrowList(@org.apache.ibatis.annotations.Param("userId") Long userId);

    /** 查询所有逾期未还的记录（用于生成罚金） */
    public List<LibBorrow> selectOverdueBorrows();
}
