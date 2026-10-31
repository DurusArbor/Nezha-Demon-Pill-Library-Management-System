package com.ruoyi.system.controller;

import java.util.List;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.system.domain.LibReserve;
import com.ruoyi.system.service.ILibReserveService;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 图书预约Controller
 * 
 * @author ruoyi
 */
@RestController
@RequestMapping("/system/reserve")
public class LibReserveController extends BaseController
{
    @Autowired
    private ILibReserveService libReserveService;

    /**
     * 查询图书预约列表
     */
    @PreAuthorize("@ss.hasPermi('system:reserve:list')")
    @GetMapping("/list")
    public TableDataInfo list(LibReserve libReserve)
    {
        startPage();
        List<LibReserve> list = libReserveService.selectLibReserveList(libReserve);
        return getDataTable(list);
    }

    /**
     * 获取图书预约详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:reserve:query')")
    @GetMapping(value = "/{reserveId}")
    public AjaxResult getInfo(@PathVariable("reserveId") Long reserveId)
    {
        return success(libReserveService.selectLibReserveByReserveId(reserveId));
    }

    /**
     * 取消预约（管理员代操作）
     */
    @PreAuthorize("@ss.hasPermi('system:reserve:edit')")
    @Log(title = "图书预约", businessType = BusinessType.UPDATE)
    @PostMapping("/cancel/{reserveId}")
    public AjaxResult cancel(@PathVariable("reserveId") Long reserveId)
    {
        libReserveService.cancel(reserveId, null);
        return success("已取消预约");
    }
}
