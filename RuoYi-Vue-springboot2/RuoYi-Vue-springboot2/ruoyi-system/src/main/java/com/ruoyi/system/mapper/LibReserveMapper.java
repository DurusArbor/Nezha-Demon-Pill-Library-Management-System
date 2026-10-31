package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.LibReserve;

/**
 * 图书预约Mapper接口
 * 
 * @author ruoyi
 */
public interface LibReserveMapper 
{
    /** 查询图书预约 */
    public LibReserve selectLibReserveByReserveId(Long reserveId);

    /** 查询图书预约列表 */
    public List<LibReserve> selectLibReserveList(LibReserve libReserve);

    /** 新增图书预约 */
    public int insertLibReserve(LibReserve libReserve);

    /** 修改图书预约 */
    public int updateLibReserve(LibReserve libReserve);

    /** 删除图书预约 */
    public int deleteLibReserveByReserveId(Long reserveId);

    /** 批量删除图书预约 */
    public int deleteLibReserveByReserveIds(Long[] reserveIds);

    /** 查询某读者对某书的有效预约（预约中/已到书） */
    public LibReserve selectActiveByUserAndBook(@org.apache.ibatis.annotations.Param("userId") Long userId,
                                                @org.apache.ibatis.annotations.Param("bookId") Long bookId);

    /** 查询某书最早的等待中预约（用于还书后到书提醒） */
    public LibReserve selectWaitingByBookId(@org.apache.ibatis.annotations.Param("bookId") Long bookId);

    /** 查询某读者的预约列表（含书名、读者名） */
    public List<LibReserve> selectMyReserveList(@org.apache.ibatis.annotations.Param("userId") Long userId);

    /** 预约到书后过期处理：已到书待借超过保留截止时间置为已过期 */
    public int expireOverdueReserves();
}
