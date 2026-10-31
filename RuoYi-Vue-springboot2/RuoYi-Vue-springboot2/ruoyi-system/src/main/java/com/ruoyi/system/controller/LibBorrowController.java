package com.ruoyi.system.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.system.domain.LibBorrow;
import com.ruoyi.system.service.ILibBorrowService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 图书借阅记录Controller
 * 
 * @author ruoyi
 * @date 2026-10-19
 */
@RestController
@RequestMapping("/system/borrow")
public class LibBorrowController extends BaseController
{
    @Autowired
    private ILibBorrowService libBorrowService;

    /**
     * 查询图书借阅记录列表
     */
    @PreAuthorize("@ss.hasPermi('system:borrow:list')")
    @GetMapping("/list")
    public TableDataInfo list(LibBorrow libBorrow)
    {
        // 先执行逾期扫描（在 startPage 之前，避免扫描查询被分页拦截），保证列表状态实时
        libBorrowService.scanOverdue();
        startPage();
        List<LibBorrow> list = libBorrowService.selectLibBorrowList(libBorrow);
        return getDataTable(list);
    }

    /**
     * 导出图书借阅记录列表
     */
    @PreAuthorize("@ss.hasPermi('system:borrow:export')")
    @Log(title = "图书借阅记录", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, LibBorrow libBorrow)
    {
        List<LibBorrow> list = libBorrowService.selectLibBorrowList(libBorrow);
        ExcelUtil<LibBorrow> util = new ExcelUtil<LibBorrow>(LibBorrow.class);
        util.exportExcel(response, list, "图书借阅记录数据");
    }

    /**
     * 获取图书借阅记录详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:borrow:query')")
    @GetMapping(value = "/{borrowId}")
    public AjaxResult getInfo(@PathVariable("borrowId") Long borrowId)
    {
        return success(libBorrowService.selectLibBorrowByBorrowId(borrowId));
    }

    /**
     * 新增图书借阅记录
     */
    @PreAuthorize("@ss.hasPermi('system:borrow:add')")
    @Log(title = "图书借阅记录", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody LibBorrow libBorrow)
    {
        return toAjax(libBorrowService.insertLibBorrow(libBorrow));
    }

    /**
     * 修改图书借阅记录
     */
    @PreAuthorize("@ss.hasPermi('system:borrow:edit')")
    @Log(title = "图书借阅记录", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody LibBorrow libBorrow)
    {
        return toAjax(libBorrowService.updateLibBorrow(libBorrow));
    }

    /**
     * 删除图书借阅记录
     */
    @PreAuthorize("@ss.hasPermi('system:borrow:remove')")
    @Log(title = "图书借阅记录", businessType = BusinessType.DELETE)
	@DeleteMapping("/{borrowIds}")
    public AjaxResult remove(@PathVariable Long[] borrowIds)
    {
        return toAjax(libBorrowService.deleteLibBorrowByBorrowIds(borrowIds));
    }

    /**
     * 借书业务
     */
    @Log(title = "图书借阅", businessType = BusinessType.INSERT)
    @PostMapping("/doBorrow")
    public AjaxResult doBorrow(@RequestParam Long userId, @RequestParam Long bookId)
    {
        libBorrowService.doBorrow(userId, bookId);
        return success("借书成功");
    }

    /**
     * 还书业务
     */
    @Log(title = "图书归还", businessType = BusinessType.UPDATE)
    @PostMapping("/doReturn/{borrowId}")
    public AjaxResult doReturn(@PathVariable Long borrowId)
    {
        libBorrowService.doReturn(borrowId);
        return success("归还成功");
    }

    /**
     * 续借业务（管理员代操作）
     */
    @Log(title = "图书续借", businessType = BusinessType.UPDATE)
    @PostMapping("/renew/{borrowId}")
    public AjaxResult renew(@PathVariable Long borrowId)
    {
        libBorrowService.renew(borrowId);
        return success("续借成功");
    }
}
