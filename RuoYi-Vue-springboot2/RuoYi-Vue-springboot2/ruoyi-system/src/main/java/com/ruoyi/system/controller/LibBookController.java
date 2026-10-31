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
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.system.domain.LibBook;
import com.ruoyi.system.service.ILibBookService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 图书信息Controller
 * 
 * @author ruoyi
 * @date 2026-10-19
 */
@RestController
@RequestMapping("/system/book")
public class LibBookController extends BaseController
{
    @Autowired
    private ILibBookService libBookService;

    /**
     * 查询图书信息列表
     */
    @PreAuthorize("@ss.hasPermi('system:book:list')")
    @GetMapping("/list")
    public TableDataInfo list(LibBook libBook)
    {
        startPage();
        List<LibBook> list = libBookService.selectLibBookList(libBook);
        return getDataTable(list);
    }

    /**
     * 导出图书信息列表
     */
    @PreAuthorize("@ss.hasPermi('system:book:export')")
    @Log(title = "图书信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, LibBook libBook)
    {
        List<LibBook> list = libBookService.selectLibBookList(libBook);
        ExcelUtil<LibBook> util = new ExcelUtil<LibBook>(LibBook.class);
        util.exportExcel(response, list, "图书信息数据");
    }

    /**
     * 获取图书信息详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:book:query')")
    @GetMapping(value = "/{bookId}")
    public AjaxResult getInfo(@PathVariable("bookId") Long bookId)
    {
        return success(libBookService.selectLibBookByBookId(bookId));
    }

    /**
     * 新增图书信息
     */
    @PreAuthorize("@ss.hasPermi('system:book:add')")
    @Log(title = "图书信息", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody LibBook libBook)
    {
        return toAjax(libBookService.insertLibBook(libBook));
    }

    /**
     * 修改图书信息
     */
    @PreAuthorize("@ss.hasPermi('system:book:edit')")
    @Log(title = "图书信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody LibBook libBook)
    {
        return toAjax(libBookService.updateLibBook(libBook));
    }

    /**
     * 删除图书信息
     */
    @PreAuthorize("@ss.hasPermi('system:book:remove')")
    @Log(title = "图书信息", businessType = BusinessType.DELETE)
	@DeleteMapping("/{bookIds}")
    public AjaxResult remove(@PathVariable Long[] bookIds)
    {
        return toAjax(libBookService.deleteLibBookByBookIds(bookIds));
    }
}
