package com.ruoyi.system.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.system.mapper.LibStatsMapper;

/**
 * 图书馆数据统计 Controller
 */
@RestController
@RequestMapping("/system/stats")
public class LibStatsController extends BaseController
{
    @Autowired
    private LibStatsMapper libStatsMapper;

    /** 首页数据看板：借阅趋势 + 热门图书 + 逾期率 + 魔丸灵珠分布 */
    @GetMapping("/dashboard")
    public AjaxResult dashboard() {
        Map<String, Object> data = new HashMap<>();
        data.put("trend", libStatsMapper.borrowTrend(30));
        data.put("hotBooks", libStatsMapper.hotBooks(5));
        data.put("overdueRate", libStatsMapper.overdueStats());
        data.put("stock", libStatsMapper.stockTotal());
        data.put("natureDist", libStatsMapper.natureDist());
        return success(data);
    }

    /** 近 30 天借阅趋势 */
    @GetMapping("/borrowTrend")
    public AjaxResult borrowTrend() {
        Map<String, Object> data = new HashMap<>();
        data.put("trend", libStatsMapper.borrowTrend(30));
        return success(data);
    }

    /** 热门图书 Top5 */
    @GetMapping("/hotBooks")
    public AjaxResult hotBooks() {
        return success(libStatsMapper.hotBooks(5));
    }

    /** 借阅热力图（周几 × 小时） */
    @GetMapping("/heatmap")
    public AjaxResult heatmap() {
        return success(libStatsMapper.borrowHeatmap());
    }

    /** 读者画像：分类分布 + 推荐 */
    @GetMapping("/readerProfile/{userId}")
    public AjaxResult readerProfile(@PathVariable Long userId) {
        List<Map<String, Object>> dist = libStatsMapper.readerCategoryDist(userId);
        Long topCategoryId = null;
        if (dist != null && !dist.isEmpty()) {
            Object o = dist.get(0).get("categoryId");
            if (o != null) topCategoryId = Long.valueOf(o.toString());
        }
        List<Map<String, Object>> recs = topCategoryId == null
                ? java.util.Collections.emptyList()
                : libStatsMapper.recommendBooks(userId, topCategoryId, 3);
        Map<String, Object> data = new HashMap<>();
        data.put("categoryDist", dist);
        data.put("topCategoryId", topCategoryId);
        data.put("recommendations", recs);
        return success(data);
    }
}
