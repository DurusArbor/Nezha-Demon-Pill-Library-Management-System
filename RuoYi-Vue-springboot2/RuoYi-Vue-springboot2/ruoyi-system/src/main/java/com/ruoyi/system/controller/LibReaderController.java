package com.ruoyi.system.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.system.domain.LibBook;
import com.ruoyi.system.domain.LibBorrow;
import com.ruoyi.system.domain.LibCategory;
import com.ruoyi.system.domain.LibFine;
import com.ruoyi.system.domain.LibRemind;
import com.ruoyi.system.domain.LibReserve;
import com.ruoyi.system.domain.LibUser;
import com.ruoyi.system.domain.SysNotice;
import com.ruoyi.system.mapper.LibUserMapper;
import com.ruoyi.system.service.ILibBookService;
import com.ruoyi.system.service.ILibBorrowService;
import com.ruoyi.system.service.ILibCultivationService;
import com.ruoyi.system.service.ILibCategoryService;
import com.ruoyi.system.service.ILibFineService;
import com.ruoyi.system.service.ILibRemindService;
import com.ruoyi.system.service.ILibReserveService;
import com.ruoyi.system.service.ISysNoticeService;

/**
 * 读者自助端Controller（登录后自助借书/还书/续借/预约）
 * 
 * @author ruoyi
 */
@RestController
@RequestMapping("/reader")
public class LibReaderController extends BaseController
{
    @Autowired
    private ILibBookService libBookService;

    @Autowired
    private ILibBorrowService libBorrowService;

    @Autowired
    private ILibReserveService libReserveService;

    @Autowired
    private ILibFineService libFineService;

    @Autowired
    private ILibRemindService libRemindService;

    @Autowired
    private ISysNoticeService sysNoticeService;

    @Autowired
    private ILibCategoryService libCategoryService;

    @Autowired
    private ILibCultivationService libCultivationService;

    @Autowired
    private LibUserMapper libUserMapper;

    /** 获取当前登录账号对应的读者信息 */
    private LibUser getCurrentLibUser() {
        Long sysUserId = SecurityUtils.getUserId();
        LibUser user = libUserMapper.selectLibUserBySysUserId(sysUserId);
        if (user == null) {
            throw new ServiceException("当前账号未关联读者信息，请先注册或联系管理员绑定");
        }
        if ("1".equals(user.getUserStatus())) {
            throw new ServiceException("您的读者账户已被冻结，请联系管理员");
        }
        return user;
    }

    /** 获取当前登录账号对应的读者信息（允许冻结读者访问，用于归还逾期图书，保证逾期自动冻结后仍能还书解冻） */
    private LibUser getCurrentLibUserAllowFrozen() {
        Long sysUserId = SecurityUtils.getUserId();
        LibUser user = libUserMapper.selectLibUserBySysUserId(sysUserId);
        if (user == null) {
            throw new ServiceException("当前账号未关联读者信息，请先注册或联系管理员绑定");
        }
        return user;
    }

    /**
     * 在线书城：图书列表（含可借数量）
     */
    @GetMapping("/books")
    public TableDataInfo books(LibBook libBook)
    {
        startPage();
        return getDataTable(libBookService.selectLibBookList(libBook));
    }

    /**
     * 图书分类（书城筛选用，读者登录即可访问，无需管理权限）
     */
    @GetMapping("/categories")
    public AjaxResult categories()
    {
        List<LibCategory> list = libCategoryService.selectLibCategoryList(new LibCategory());
        return success(list);
    }

    /**
     * 我的借阅列表（冻结读者也可查看，便于归还逾期图书）
     */
    @GetMapping("/myBorrows")
    public AjaxResult myBorrows()
    {
        return success(libBorrowService.selectMyBorrows(getCurrentLibUserAllowFrozen().getUserId()));
    }

    /**
     * 在线借书
     */
    @PostMapping("/borrow")
    public AjaxResult borrow(@RequestBody LibBorrow body)
    {
        Long bookId = body.getBookId();
        if (bookId == null) {
            throw new ServiceException("请选择图书");
        }
        LibUser user = getCurrentLibUser();
        libBorrowService.doBorrow(user.getUserId(), bookId);
        // 若该读者对这本书有预约，自动完结预约
        libReserveService.completeReserveOnBorrow(user.getUserId(), bookId);
        return success("借阅成功，请按时归还");
    }

    /**
     * 在线还书（冻结读者也可归还，还清后自动解冻）
     */
    @PostMapping("/return/{borrowId}")
    public AjaxResult returnBook(@PathVariable Long borrowId)
    {
        LibUser user = getCurrentLibUserAllowFrozen();
        LibBorrow record = libBorrowService.selectLibBorrowByBorrowId(borrowId);
        if (record == null) {
            throw new ServiceException("借阅记录不存在");
        }
        if (!user.getUserId().equals(record.getUserId())) {
            throw new ServiceException("只能归还自己的借阅记录");
        }
        libBorrowService.doReturn(borrowId);
        return success("归还成功");
    }

    /**
     * 续借
     */
    @PostMapping("/renew/{borrowId}")
    public AjaxResult renew(@PathVariable Long borrowId)
    {
        LibUser user = getCurrentLibUser();
        LibBorrow record = libBorrowService.selectLibBorrowByBorrowId(borrowId);
        if (record == null) {
            throw new ServiceException("借阅记录不存在");
        }
        if (!user.getUserId().equals(record.getUserId())) {
            throw new ServiceException("只能续借自己的借阅记录");
        }
        libBorrowService.renew(borrowId);
        return success("续借成功");
    }

    /**
     * 我的预约列表
     */
    @GetMapping("/myReserves")
    public AjaxResult myReserves()
    {
        return success(libReserveService.selectMyReserves(getCurrentLibUser().getUserId()));
    }

    /**
     * 预约图书
     */
    @PostMapping("/reserve")
    public AjaxResult reserve(@RequestBody LibReserve body)
    {
        Long bookId = body.getBookId();
        if (bookId == null) {
            throw new ServiceException("请选择图书");
        }
        libReserveService.reserve(getCurrentLibUser().getUserId(), bookId);
        return success("预约成功，归还后我们会通知您");
    }

    /**
     * 取消预约
     */
    @DeleteMapping("/reserve/{reserveId}")
    public AjaxResult cancelReserve(@PathVariable Long reserveId)
    {
        libReserveService.cancel(reserveId, getCurrentLibUser().getUserId());
        return success("已取消预约");
    }

    /**
     * 我的罚金列表
     */
    @GetMapping("/myFines")
    public AjaxResult myFines()
    {
        return success(libFineService.selectMyFines(getCurrentLibUser().getUserId()));
    }

    /**
     * 读者自助缴纳罚金（仅允许缴纳本人的罚金）
     */
    @PostMapping("/fine/pay/{fineId}")
    public AjaxResult payMyFine(@PathVariable Long fineId)
    {
        LibUser user = getCurrentLibUser();
        List<LibFine> myFines = libFineService.selectMyFines(user.getUserId());
        boolean mine = false;
        for (LibFine f : myFines) {
            if (fineId.equals(f.getFineId())) {
                mine = true;
                break;
            }
        }
        if (!mine) {
            throw new ServiceException("只能缴纳本人的罚金");
        }
        // 免罚令自动抵消：持有免罚令则消耗一张，免去本次缴纳
        int tokens = user.getForgiveTokens() == null ? 0 : user.getForgiveTokens();
        if (tokens > 0) {
            libUserMapper.updateForgiveTokens(user.getUserId(), -1);
            libCultivationService.change(user.getUserId(), 0, 0, "forgive_use", "使用免罚令自动抵消罚金");
            libFineService.pay(fineId);
            return success("已使用免罚令自动抵消罚金");
        }
        libFineService.pay(fineId);
        return success("已确认缴纳");
    }

    /**
     * 开仙丹盲盒：20 功德/次，随机 +10~20 功德，每日最多 3 次，魔丸不可抽
     */
    @PostMapping("/lottery")
    public AjaxResult lottery()
    {
        Long userId = getCurrentLibUser().getUserId();
        return success(libCultivationService.lottery(userId));
    }

    /**
     * 兑换免罚令：50 功德/张，缴罚金时自动抵消一次
     */
    @PostMapping("/token/buy")
    public AjaxResult buyForgiveToken()
    {
        Long userId = getCurrentLibUser().getUserId();
        return success(libCultivationService.buyForgiveToken(userId));
    }

    /**
     * 我的催缴通知（默认返回未读，可传 status 过滤）
     */
    @GetMapping("/reminds")
    public AjaxResult reminds(String status)
    {
        Long userId = getCurrentLibUser().getUserId();
        return success(libRemindService.selectMyReminds(userId, status));
    }

    /**
     * 标记某条催缴为已读
     */
    @PostMapping("/reminds/read/{remindId}")
    public AjaxResult readRemind(@PathVariable Long remindId)
    {
        Long userId = getCurrentLibUser().getUserId();
        libRemindService.markRead(remindId, userId);
        return success("已确认");
    }

    /**
     * 图书馆公告（读者端，登录即可访问，仅返回正常状态公告，支持分页）
     */
    @GetMapping("/notices")
    public TableDataInfo notices(SysNotice notice)
    {
        notice.setStatus("0");
        startPage();
        List<SysNotice> list = sysNoticeService.selectNoticeList(notice);
        return getDataTable(list);
    }

    /**
     * 我的修行（境界、魔丸/灵珠、功德魔性、进度）
     */
    @GetMapping("/cultivation")
    public AjaxResult cultivation()
    {
        return success(libCultivationService.getCultivation(getCurrentLibUser().getUserId()));
    }

    /**
     * 每日签到
     */
    @PostMapping("/sign")
    public AjaxResult sign()
    {
        Long userId = getCurrentLibUser().getUserId();
        int gain = libCultivationService.sign(userId);
        if (gain == 0) {
            return success("今日已签到，明日再来");
        }
        return success("签到成功，功德 +" + gain);
    }

    /**
     * 每日奇遇/天劫（每天最多一次，20% 概率触发）
     */
    @PostMapping("/fortune")
    public AjaxResult fortune()
    {
        return success(libCultivationService.fortune(getCurrentLibUser().getUserId()));
    }

    /**
     * 魔性净化仪式（一念成仙）：消耗 100 功德净化 10 点魔性
     */
    @PostMapping("/purify")
    public AjaxResult purify()
    {
        AjaxResult r = success(libCultivationService.purify(getCurrentLibUser().getUserId()));
        r.put("msg", "魔性消退，一念成仙！");
        return r;
    }

    /**
     * 封神榜（功德排行榜）
     */
    @GetMapping("/rank")
    public AjaxResult rank()
    {
        return success(libCultivationService.getRankList(1000));
    }
}
