package com.ruoyi.system.service.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.system.domain.LibFine;
import com.ruoyi.system.domain.SysNotice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.LibBorrowMapper;
import com.ruoyi.system.mapper.LibBookMapper;
import com.ruoyi.system.mapper.LibFineMapper;
import com.ruoyi.system.mapper.LibUserMapper;
import com.ruoyi.system.domain.LibBorrow;
import com.ruoyi.system.domain.LibBook;
import com.ruoyi.system.domain.LibReserve;
import com.ruoyi.system.domain.LibUser;
import com.ruoyi.system.service.ILibBorrowService;
import com.ruoyi.system.service.ILibCultivationService;
import com.ruoyi.system.service.ILibReserveService;
import com.ruoyi.system.service.ISysConfigService;
import com.ruoyi.system.service.ISysNoticeService;

/**
 * 图书借阅记录Service业务层处理
 *
 * @author ruoyi
 */
@Service
public class LibBorrowServiceImpl implements ILibBorrowService
{
    @Autowired
    private LibBorrowMapper libBorrowMapper;

    @Autowired
    private LibBookMapper libBookMapper;

    @Autowired
    private LibUserMapper libUserMapper;

    @Autowired
    private LibFineMapper libFineMapper;

    @Autowired
    private ISysConfigService configService;

    @Autowired
    private ILibReserveService reserveService;

    @Autowired
    private ISysNoticeService noticeService;

    @Autowired
    private ILibCultivationService cultivationService;

    /** 读取整数参数 */
    private int getConfigInt(String key, int def) {
        String v = configService.selectConfigByKey(key);
        try {
            return (v == null || v.trim().isEmpty()) ? def : Integer.parseInt(v.trim());
        } catch (NumberFormatException e) {
            return def;
        }
    }

    /** 读取小数参数 */
    private double getConfigDouble(String key, double def) {
        String v = configService.selectConfigByKey(key);
        try {
            return (v == null || v.trim().isEmpty()) ? def : Double.parseDouble(v.trim());
        } catch (NumberFormatException e) {
            return def;
        }
    }

    @Override
    public LibBorrow selectLibBorrowByBorrowId(Long borrowId) {
        return libBorrowMapper.selectLibBorrowByBorrowId(borrowId);
    }

    @Override
    public List<LibBorrow> selectLibBorrowList(LibBorrow libBorrow) {
        // 注意：此处不做扫描，扫描由 LibBorrowController.list 在 startPage 之前调用，
        // 避免扫描中的 SELECT 被 PageHelper 分页拦截导致数据不全
        return libBorrowMapper.selectLibBorrowList(libBorrow);
    }

    @Override
    public int insertLibBorrow(LibBorrow libBorrow) {
        libBorrow.setCreateTime(DateUtils.getNowDate());
        return libBorrowMapper.insertLibBorrow(libBorrow);
    }

    @Override
    public int updateLibBorrow(LibBorrow libBorrow) {
        libBorrow.setUpdateTime(DateUtils.getNowDate());
        return libBorrowMapper.updateLibBorrow(libBorrow);
    }

    @Override
    public int deleteLibBorrowByBorrowIds(Long[] borrowIds) {
        return libBorrowMapper.deleteLibBorrowByBorrowIds(borrowIds);
    }

    @Override
    public int deleteLibBorrowByBorrowId(Long borrowId) {
        return libBorrowMapper.deleteLibBorrowByBorrowId(borrowId);
    }

    /**
     * 借书业务闭环（借阅期限与上限由系统参数控制）
     */
    @Override
    public void doBorrow(Long userId, Long bookId) {
        int borrowDays = getConfigInt("lib.borrow.days", 30);
        int maxBooks = getConfigInt("lib.borrow.max", 5);
        // 1. 校验读者
        LibUser user = libUserMapper.selectLibUserByUserId(userId);
        if (user == null) {
            throw new ServiceException("读者不存在");
        }
        if ("1".equals(user.getUserStatus())) {
            throw new ServiceException("该读者已被冻结，不能借书");
        }
        // 2. 校验图书
        LibBook book = libBookMapper.selectLibBookByBookId(bookId);
        if (book == null) {
            throw new ServiceException("图书不存在");
        }
        if ("1".equals(book.getBookStatus())) {
            throw new ServiceException("该书已下架，不能借阅");
        }
        long available = book.getBookCount() == null ? 0 : book.getBookCount()
                       - (book.getBorrowCount() == null ? 0 : book.getBorrowCount());
        if (available <= 0) {
            throw new ServiceException("该书馆藏已借完，请等待归还或预约");
        }
        // 3. 校验借阅上限
        int activeCount = libBorrowMapper.countActiveByUserId(userId);
        if (activeCount >= maxBooks) {
            throw new ServiceException("该读者未还图书已达 " + maxBooks + " 本上限，请先归还");
        }
        // 4. 校验是否重复借同一本
        LibBorrow exist = libBorrowMapper.selectActiveByUserAndBook(userId, bookId);
        if (exist != null) {
            throw new ServiceException("该读者已借阅此书且尚未归还");
        }
        // 5. 写借阅记录
        Date now = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(now);
        c.add(Calendar.DAY_OF_MONTH, borrowDays);
        LibBorrow record = new LibBorrow();
        record.setUserId(userId);
        record.setBookId(bookId);
        record.setBorrowTime(now);
        record.setDeadlineTime(c.getTime());
        record.setBorrowStatus("0");
        record.setRenewCount(0);
        record.setCreateTime(now);
        libBorrowMapper.insertLibBorrow(record);
        // 6. 扣库存
        libBookMapper.increBorrowCount(bookId);
        // 7. 修行：借书功德 +5
        cultivationService.change(userId, 5, 0, "borrow", "借阅图书，功德 +5");
    }

    /**
     * 还书业务闭环（触发预约到书提醒；逾期自动补生成罚金）
     */
    @Override
    public void doReturn(Long borrowId) {
        LibBorrow record = libBorrowMapper.selectLibBorrowByBorrowId(borrowId);
        if (record == null) {
            throw new ServiceException("借阅记录不存在");
        }
        if ("1".equals(record.getBorrowStatus())) {
            throw new ServiceException("该记录已是已归还状态");
        }
        // 1. 更新借阅记录
        LibBorrow update = new LibBorrow();
        update.setBorrowId(borrowId);
        update.setReturnTime(new Date());
        update.setBorrowStatus("1");
        update.setUpdateTime(new Date());
        libBorrowMapper.updateLibBorrow(update);
        // 2. 回补库存
        libBookMapper.decreBorrowCount(record.getBookId());
        // 3. 触发预约到书提醒
        LibReserve activated = reserveService.activateWaitingReserveOnReturn(record.getBookId());
        if (activated != null) {
            LibBook book = libBookMapper.selectLibBookByBookId(record.getBookId());
            int keepDays = 3;
            String v = configService.selectConfigByKey("lib.reserve.days");
            try { keepDays = (v == null || v.trim().isEmpty()) ? 3 : Integer.parseInt(v.trim()); } catch (NumberFormatException e) { }
            SysNotice notice = new SysNotice();
            notice.setNoticeTitle("预约到书提醒：《" + (book != null ? book.getBookName() : "未知图书") + "》");
            notice.setNoticeType("2");
            notice.setNoticeContent("您预约的图书已到书，请在 " + keepDays + " 天内到馆借出，超期预约将自动失效。");
            notice.setStatus("0");
            notice.setCreateBy("system");
            notice.setCreateTime(new Date());
            noticeService.insertNotice(notice);
        }
        // 4. 逾期罚金：还书时若已逾期且未生成罚金，补生成（魔性已随逾期天数推进+罚金生成时结算，还书不再补记）
        boolean overdue = record.getDeadlineTime() != null && record.getDeadlineTime().before(new Date());
        if (overdue) {
            if (libFineMapper.selectLibFineByBorrowId(borrowId) == null) {
                insertFine(record);
            }
            cultivationService.change(record.getUserId(), 2, 0,
                    "return_overdue", "逾期归还，功德 +2");
        } else {
            // 5. 修行：按时归还功德 +10，莲花净化魔性 -2
            cultivationService.change(record.getUserId(), 10, -2,
                    "return_on_time", "按时归还，功德 +10，莲花净化魔性");
        }
        // 6. 还书后自动解冻：若无其他超期未还（超过 lib.freeze.days 天）记录则解冻
        int freezeDays = getConfigInt("lib.freeze.days", 3);
        if (libUserMapper.countLongOverdueByUserId(record.getUserId(), freezeDays) == 0) {
            libUserMapper.unfreezeLibUser(record.getUserId());
        }
    }

    /**
     * 续借业务
     */
    @Override
    public void renew(Long borrowId) {
        LibBorrow record = libBorrowMapper.selectLibBorrowByBorrowId(borrowId);
        if (record == null) {
            throw new ServiceException("借阅记录不存在");
        }
        if (!"0".equals(record.getBorrowStatus())) {
            throw new ServiceException("仅借阅中的图书可以续借，逾期图书请先归还");
        }
        int renewTimes = getConfigInt("lib.renew.times", 1);
        int renewDays = getConfigInt("lib.renew.days", 15);
        int used = record.getRenewCount() == null ? 0 : record.getRenewCount();
        if (used >= renewTimes) {
            throw new ServiceException("已达续借次数上限（" + renewTimes + " 次），无法继续续借");
        }
        libBorrowMapper.updateRenew(borrowId, renewDays);
    }

    /**
     * 查询某读者的借阅列表
     */
    @Override
    public List<LibBorrow> selectMyBorrows(Long userId) {
        libBorrowMapper.markOverdueRecords();
        settleOverdueDemon();
        genFines();
        freezeLongOverdueReaders();
        reserveService.expireOverdueReserves();
        return libBorrowMapper.selectMyBorrowList(userId);
    }

    /**
     * 扫描逾期：标记逾期、推进逾期魔性、生成罚金、冻结长期逾期读者、处理预约过期（由定时任务调用）
     */
    @Override
    public void scanOverdue() {
        libBorrowMapper.markOverdueRecords();
        settleOverdueDemon();
        genFines();
        freezeLongOverdueReaders();
        reserveService.expireOverdueReserves();
    }

    /** 逾期魔性随天数推进：每逾期一天 +5（幂等，只补已结算天数之后的差额） */
    private void settleOverdueDemon() {
        List<LibBorrow> overdueList = libBorrowMapper.selectOverdueBorrows();
        for (LibBorrow b : overdueList) {
            if (b.getDeadlineTime() == null) {
                continue;
            }
            int curDays = DateUtils.differentDaysByMillisecond(b.getDeadlineTime(), new Date());
            if (curDays < 1) {
                curDays = 1;
            }
            int settled = b.getDemonSettledDays() == null ? 0 : b.getDemonSettledDays();
            if (curDays > settled) {
                int delta = (curDays - settled) * 5;
                cultivationService.change(b.getUserId(), 0, delta, "overdue_demon",
                        "逾期魔性推进（第 " + settled + "~" + curDays + " 天），每逾期一天 +5，魔性 +" + delta);
                libBorrowMapper.updateDemonSettledDays(b.getBorrowId(), curDays);
            }
        }
    }

    /** 逾期超过 N 天未还直接冻结读者（默认 3 天，可由系统参数 lib.freeze.days 调整） */
    private void freezeLongOverdueReaders() {
        int freezeDays = getConfigInt("lib.freeze.days", 3);
        libUserMapper.freezeLongOverdueReaders(freezeDays);
    }

    /** 为所有逾期未缴的借阅记录生成罚金（幂等） */
    private void genFines() {
        List<LibBorrow> overdueList = libBorrowMapper.selectOverdueBorrows();
        for (LibBorrow b : overdueList) {
            if (libFineMapper.selectLibFineByBorrowId(b.getBorrowId()) != null) {
                continue;
            }
            insertFine(b);
        }
    }

    /** 计算并写入一条罚金 */
    private void insertFine(LibBorrow b) {
        double perday = getConfigDouble("lib.fine.perday", 0.5);
        int days = DateUtils.differentDaysByMillisecond(b.getDeadlineTime(), new Date());
        if (days < 1) {
            days = 1;
        }
        LibFine fine = new LibFine();
        fine.setBorrowId(b.getBorrowId());
        fine.setUserId(b.getUserId());
        fine.setBookId(b.getBookId());
        fine.setOverdueDays(days);
        fine.setFineAmount(days * perday);
        fine.setFineStatus("0");
        fine.setCreateTime(new Date());
        libFineMapper.insertLibFine(fine);
        // 罚金生成时同步加魔性：罚金金额的 10 倍
        int demon = (int) Math.round(days * perday * 10);
        cultivationService.change(b.getUserId(), 0, demon, "fine_demon",
                "逾期罚金生成（" + fine.getFineAmount() + " 元），魔性 +" + demon);
    }
}
