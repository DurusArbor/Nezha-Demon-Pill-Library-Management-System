package com.ruoyi.system.controller;

import java.util.List;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.system.domain.LibFine;
import com.ruoyi.system.service.ILibFineService;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 借阅罚金Controller
 * 
 * @author ruoyi
 */
@RestController
@RequestMapping("/system/fine")
public class LibFineController extends BaseController
{
    @Autowired
    private ILibFineService libFineService;

    /**
     * 查询罚金列表
     */
    @PreAuthorize("@ss.hasPermi('system:fine:list')")
    @GetMapping("/list")
    public TableDataInfo list(LibFine libFine)
    {
        startPage();
        List<LibFine> list = libFineService.selectLibFineList(libFine);
        return getDataTable(list);
    }

    /**
     * 确认缴纳罚金
     */
    @PreAuthorize("@ss.hasPermi('system:fine:edit')")
    @Log(title = "罚金缴纳", businessType = BusinessType.UPDATE)
    @PostMapping("/pay/{fineId}")
    public AjaxResult pay(@PathVariable("fineId") Long fineId)
    {
        libFineService.pay(fineId);
        return success("已确认缴纳");
    }
}
