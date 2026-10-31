package com.ruoyi.system.controller;

import java.util.List;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.system.domain.LibRemind;
import com.ruoyi.system.service.ILibRemindService;

/**
 * 逾期催缴管理 Controller
 *
 * @author ruoyi
 */
@RestController
@RequestMapping("/system/remind")
public class LibRemindController extends BaseController
{
    @Autowired
    private ILibRemindService libRemindService;

    /**
     * 催缴记录列表
     */
    @PreAuthorize("@ss.hasPermi('system:fine:list')")
    @GetMapping("/list")
    public TableDataInfo list(LibRemind libRemind)
    {
        startPage();
        List<LibRemind> list = libRemindService.selectLibRemindList(libRemind);
        return getDataTable(list);
    }

    /**
     * 对某笔逾期借阅发起催缴（读者端会收到催缴弹窗）
     */
    @PreAuthorize("@ss.hasPermi('system:fine:edit')")
    @Log(title = "逾期催缴", businessType = BusinessType.INSERT)
    @PostMapping("/urge/{borrowId}")
    public AjaxResult urge(@PathVariable Long borrowId)
    {
        return success(libRemindService.urge(borrowId));
    }
}
