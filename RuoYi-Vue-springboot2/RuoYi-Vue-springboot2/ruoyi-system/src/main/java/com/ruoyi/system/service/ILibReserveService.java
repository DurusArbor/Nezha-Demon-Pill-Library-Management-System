package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.LibReserve;

/**
 * 图书预约Service接口
 * 
 * @author ruoyi
 */
public interface ILibReserveService 
{
    /** 查询图书预约列表 */
    public List<LibReserve> selectLibReserveList(LibReserve libReserve);

    /** 查询图书预约 */
    public LibReserve selectLibReserveByReserveId(Long reserveId);

    /** 读者预约图书 */
    public void reserve(Long userId, Long bookId);

    /** 取消预约（userId 为空时表示管理员操作） */
    public void cancel(Long reserveId, Long userId);

    /** 查询某读者的预约列表 */
    public List<LibReserve> selectMyReserves(Long userId);

    /** 还书后触发：把该书最早的等待中预约置为已到书待借，返回被激活的预约 */
    public LibReserve activateWaitingReserveOnReturn(Long bookId);

    /** 读者借出预约图书后：把对应的有效预约置为已借出 */
    public void completeReserveOnBorrow(Long userId, Long bookId);

    /** 预约过期处理：已到书待借超时置为已过期 */
    public void expireOverdueReserves();
}
