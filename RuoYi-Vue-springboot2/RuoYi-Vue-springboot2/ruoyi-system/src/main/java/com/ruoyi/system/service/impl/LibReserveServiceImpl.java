package com.ruoyi.system.service.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import com.ruoyi.common.exception.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.LibReserveMapper;
import com.ruoyi.system.mapper.LibBookMapper;
import com.ruoyi.system.mapper.LibBorrowMapper;
import com.ruoyi.system.domain.LibReserve;
import com.ruoyi.system.domain.LibBook;
import com.ruoyi.system.domain.LibBorrow;
import com.ruoyi.system.service.ILibReserveService;
import com.ruoyi.system.service.ISysConfigService;

/**
 * 图书预约Service业务层处理
 *
 * @author ruoyi
 */
@Service
public class LibReserveServiceImpl implements ILibReserveService
{
    @Autowired
    private LibReserveMapper libReserveMapper;

    @Autowired
    private LibBookMapper libBookMapper;

    @Autowired
    private LibBorrowMapper libBorrowMapper;

    @Autowired
    private ISysConfigService configService;

    @Override
    public List<LibReserve> selectLibReserveList(LibReserve libReserve) {
        return libReserveMapper.selectLibReserveList(libReserve);
    }

    @Override
    public LibReserve selectLibReserveByReserveId(Long reserveId) {
        return libReserveMapper.selectLibReserveByReserveId(reserveId);
    }

    /**
     * 读者预约图书
     */
    @Override
    public void reserve(Long userId, Long bookId) {
        LibBook book = libBookMapper.selectLibBookByBookId(bookId);
        if (book == null) {
            throw new ServiceException("图书不存在");
        }
        if ("1".equals(book.getBookStatus())) {
            throw new ServiceException("该书已下架，无法预约");
        }
        LibBorrow activeBorrow = libBorrowMapper.selectActiveByUserAndBook(userId, bookId);
        if (activeBorrow != null) {
            throw new ServiceException("您已借阅该书且尚未归还，无需预约");
        }
        LibReserve exist = libReserveMapper.selectActiveByUserAndBook(userId, bookId);
        if (exist != null) {
            throw new ServiceException("您已预约过这本书，请勿重复预约");
        }
        LibReserve record = new LibReserve();
        record.setUserId(userId);
        record.setBookId(bookId);
        record.setReserveTime(new Date());
        record.setReserveStatus("0");
        record.setCreateTime(new Date());
        libReserveMapper.insertLibReserve(record);
    }

    /**
     * 取消预约
     */
    @Override
    public void cancel(Long reserveId, Long userId) {
        LibReserve record = libReserveMapper.selectLibReserveByReserveId(reserveId);
        if (record == null) {
            throw new ServiceException("预约记录不存在");
        }
        if (userId != null && !userId.equals(record.getUserId())) {
            throw new ServiceException("只能取消自己的预约");
        }
        if (!"0".equals(record.getReserveStatus()) && !"1".equals(record.getReserveStatus())) {
            throw new ServiceException("当前状态的预约不能取消");
        }
        LibReserve update = new LibReserve();
        update.setReserveId(reserveId);
        update.setReserveStatus("3");
        update.setUpdateTime(new Date());
        libReserveMapper.updateLibReserve(update);
    }

    /**
     * 查询某读者的预约列表
     */
    @Override
    public List<LibReserve> selectMyReserves(Long userId) {
        expireOverdueReserves();
        return libReserveMapper.selectMyReserveList(userId);
    }

    /**
     * 还书后触发到书提醒
     */
    @Override
    public LibReserve activateWaitingReserveOnReturn(Long bookId) {
        LibReserve waiting = libReserveMapper.selectWaitingByBookId(bookId);
        if (waiting == null) {
            return null;
        }
        int keepDays = 3;
        String v = configService.selectConfigByKey("lib.reserve.days");
        try { keepDays = (v == null || v.trim().isEmpty()) ? 3 : Integer.parseInt(v.trim()); } catch (NumberFormatException e) { }
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DAY_OF_MONTH, keepDays);
        LibReserve update = new LibReserve();
        update.setReserveId(waiting.getReserveId());
        update.setReserveStatus("1");
        update.setExpireTime(c.getTime());
        update.setUpdateTime(new Date());
        libReserveMapper.updateLibReserve(update);
        return libReserveMapper.selectLibReserveByReserveId(waiting.getReserveId());
    }

    /**
     * 读者借出预约图书后，把有效预约置为已借出
     */
    @Override
    public void completeReserveOnBorrow(Long userId, Long bookId) {
        LibReserve active = libReserveMapper.selectActiveByUserAndBook(userId, bookId);
        if (active == null) {
            return;
        }
        LibReserve update = new LibReserve();
        update.setReserveId(active.getReserveId());
        update.setReserveStatus("2");
        update.setUpdateTime(new Date());
        libReserveMapper.updateLibReserve(update);
    }

    /**
     * 预约过期处理
     */
    @Override
    public void expireOverdueReserves() {
        libReserveMapper.expireOverdueReserves();
    }
}
