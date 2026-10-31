package com.ruoyi.system.service.impl;

import java.util.Date;
import java.util.List;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.system.domain.LibBook;
import com.ruoyi.system.domain.LibBorrow;
import com.ruoyi.system.domain.LibFine;
import com.ruoyi.system.domain.LibRemind;
import com.ruoyi.system.mapper.LibBookMapper;
import com.ruoyi.system.mapper.LibBorrowMapper;
import com.ruoyi.system.mapper.LibFineMapper;
import com.ruoyi.system.mapper.LibRemindMapper;
import com.ruoyi.system.service.ILibRemindService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 逾期催缴 Service 业务层
 *
 * @author ruoyi
 */
@Service
public class LibRemindServiceImpl implements ILibRemindService
{
    @Autowired
    private LibRemindMapper libRemindMapper;

    @Autowired
    private LibBorrowMapper libBorrowMapper;

    @Autowired
    private LibBookMapper libBookMapper;

    @Autowired
    private LibFineMapper libFineMapper;

    @Override
    public List<LibRemind> selectLibRemindList(LibRemind libRemind)
    {
        return libRemindMapper.selectLibRemindList(libRemind);
    }

    /**
     * 对某笔逾期借阅发起催缴
     */
    @Override
    public LibRemind urge(Long borrowId)
    {
        LibBorrow borrow = libBorrowMapper.selectLibBorrowByBorrowId(borrowId);
        if (borrow == null)
        {
            throw new ServiceException("借阅记录不存在");
        }
        if (!"2".equals(borrow.getBorrowStatus()))
        {
            throw new ServiceException("该记录未处于逾期状态，无需催缴");
        }

        String bookName = "图书#" + borrow.getBookId();
        LibBook book = libBookMapper.selectLibBookByBookId(borrow.getBookId());
        if (book != null && book.getBookName() != null)
        {
            bookName = book.getBookName();
        }

        StringBuilder content = new StringBuilder();
        content.append("您借阅的《").append(bookName).append("》已逾期，请尽快归还。");
        LibFine fine = libFineMapper.selectLibFineByBorrowId(borrowId);
        if (fine != null && "0".equals(fine.getFineStatus()))
        {
            content.append("当前产生罚金 ¥").append(fine.getFineAmount())
                    .append("，逾期越久罚金越高，请及时到「我的罚金」缴纳。");
        }

        LibRemind remind = new LibRemind();
        remind.setBorrowId(borrowId);
        remind.setUserId(borrow.getUserId());
        remind.setBookId(borrow.getBookId());
        remind.setRemindContent(content.toString());
        remind.setStatus("0");
        Date now = new Date();
        remind.setCreateBy(SecurityUtils.getUsername());
        remind.setCreateTime(now);
        libRemindMapper.insertLibRemind(remind);
        return remind;
    }

    @Override
    public List<LibRemind> selectMyReminds(Long userId, String status)
    {
        return libRemindMapper.selectMyRemindList(userId, status);
    }

    @Override
    public void markRead(Long remindId, Long userId)
    {
        LibRemind remind = libRemindMapper.selectLibRemindByRemindId(remindId);
        if (remind == null)
        {
            throw new ServiceException("催缴记录不存在");
        }
        if (userId != null && !userId.equals(remind.getUserId()))
        {
            throw new ServiceException("只能处理本人的催缴通知");
        }
        if ("1".equals(remind.getStatus()))
        {
            return;
        }
        LibRemind update = new LibRemind();
        update.setRemindId(remindId);
        update.setStatus("1");
        update.setUpdateTime(new Date());
        libRemindMapper.updateLibRemind(update);
    }
}
