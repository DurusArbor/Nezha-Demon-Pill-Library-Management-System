package com.ruoyi.system.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.system.domain.LibCategory;
import com.ruoyi.system.service.ILibCategoryService;

/**
 * 图书分类Controller
 *
 * @author ruoyi
 */
@RestController
@RequestMapping("/system/category")
public class LibCategoryController extends BaseController
{
    @Autowired
    private ILibCategoryService libCategoryService;

    @PreAuthorize("@ss.hasPermi('system:category:list')")
    @GetMapping("/list")
    public TableDataInfo list(LibCategory libCategory) {
        startPage();
        List<LibCategory> list = libCategoryService.selectLibCategoryList(libCategory);
        return getDataTable(list);
    }

    @GetMapping(value = "/{categoryId}")
    public AjaxResult getInfo(@PathVariable("categoryId") Long categoryId) {
        return success(libCategoryService.selectLibCategoryByCategoryId(categoryId));
    }

    @PreAuthorize("@ss.hasPermi('system:category:add')")
    @Log(title = "图书分类", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody LibCategory libCategory) {
        return toAjax(libCategoryService.insertLibCategory(libCategory));
    }

    @PreAuthorize("@ss.hasPermi('system:category:edit')")
    @Log(title = "图书分类", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody LibCategory libCategory) {
        return toAjax(libCategoryService.updateLibCategory(libCategory));
    }

    @PreAuthorize("@ss.hasPermi('system:category:remove')")
    @Log(title = "图书分类", businessType = BusinessType.DELETE)
    @DeleteMapping("/{categoryIds}")
    public AjaxResult remove(@PathVariable Long[] categoryIds) {
        return toAjax(libCategoryService.deleteLibCategoryByCategoryIds(categoryIds));
    }
}
