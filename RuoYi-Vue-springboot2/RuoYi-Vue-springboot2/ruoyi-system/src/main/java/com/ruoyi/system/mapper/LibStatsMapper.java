package com.ruoyi.system.mapper;

import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Param;

/**
 * 图书馆统计 Mapper
 */
public interface LibStatsMapper
{
    /** 近 N 天每日借阅量 */
    public List<Map<String, Object>> borrowTrend(@Param("days") int days);

    /** 热门图书 Top N（按借阅次数） */
    public List<Map<String, Object>> hotBooks(@Param("limit") int limit);

    /** 逾期统计：在借总数（未归还）、逾期笔数 */
    public Map<String, Object> overdueStats();

    /** 馆藏总册数（Σ book_count） */
    public Map<String, Object> stockTotal();

    /** 读者先天分布：魔丸/灵珠/半魔丸（按逾期、未缴罚金、魔性判定） */
    public List<Map<String, Object>> natureDist();

    /** 借阅热力图：按周几(0=周日) × 小时 */
    public List<Map<String, Object>> borrowHeatmap();

    /** 某读者历史借阅的分类分布 */
    public List<Map<String, Object>> readerCategoryDist(@Param("userId") Long userId);

    /** 推荐：指定分类下该读者未借过的热门书 Top N */
    public List<Map<String, Object>> recommendBooks(@Param("userId") Long userId,
                                                     @Param("categoryId") Long categoryId,
                                                     @Param("limit") int limit);
}
