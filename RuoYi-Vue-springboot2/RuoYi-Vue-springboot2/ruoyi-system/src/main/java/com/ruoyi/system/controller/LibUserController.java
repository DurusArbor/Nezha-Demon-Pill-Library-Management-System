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
import com.ruoyi.system.domain.LibUser;
import com.ruoyi.system.service.ILibUserService;
import com.ruoyi.system.service.ILibCultivationService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 图书读者Controller
 * 
 * @author ruoyi
 * @date 2026-10-19
 */
@RestController
@RequestMapping("/library/Libuser")
public class LibUserController extends BaseController
{
    @Autowired
    private ILibUserService libUserService;

    @Autowired
    private ILibCultivationService libCultivationService;

    /**
     * 封神榜（读者功德排行榜，管理端）
     */
    @GetMapping("/rank")
    public AjaxResult rank()
    {
        return success(libCultivationService.getRankList(1000));
    }

    /**
     * 查询图书读者列表
     */
    @PreAuthorize("@ss.hasPermi('library:Libuser:list')")
    @GetMapping("/list")
    public TableDataInfo list(LibUser libUser)
    {
        startPage();
        List<LibUser> list = libUserService.selectLibUserList(libUser);
        return getDataTable(list);
    }

    /**
     * 导出图书读者列表
     */
    @PreAuthorize("@ss.hasPermi('library:Libuser:export')")
    @Log(title = "图书读者", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, LibUser libUser)
    {
        List<LibUser> list = libUserService.selectLibUserList(libUser);
        ExcelUtil<LibUser> util = new ExcelUtil<LibUser>(LibUser.class);
        util.exportExcel(response, list, "图书读者数据");
    }

    /**
     * 获取图书读者详细信息
     */
    @PreAuthorize("@ss.hasPermi('library:Libuser:query')")
    @GetMapping(value = "/{userId}")
    public AjaxResult getInfo(@PathVariable("userId") Long userId)
    {
        return success(libUserService.selectLibUserByUserId(userId));
    }

    /**
     * 新增图书读者
     */
    @PreAuthorize("@ss.hasPermi('library:Libuser:add')")
    @Log(title = "图书读者", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody LibUser libUser)
    {
        return toAjax(libUserService.insertLibUser(libUser));
    }

    /**
     * 修改图书读者
     */
    @PreAuthorize("@ss.hasPermi('library:Libuser:edit')")
    @Log(title = "图书读者", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody LibUser libUser)
    {
        return toAjax(libUserService.updateLibUser(libUser));
    }

    /**
     * 删除图书读者
     */
    @PreAuthorize("@ss.hasPermi('library:Libuser:remove')")
    @Log(title = "图书读者", businessType = BusinessType.DELETE)
	@DeleteMapping("/{userIds}")
    public AjaxResult remove(@PathVariable Long[] userIds)
    {
        return toAjax(libUserService.deleteLibUserByUserIds(userIds));
    }
}
