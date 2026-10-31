package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.LibUser;

/**
 * 图书读者Mapper接口
 * 
 * @author ruoyi
 * @date 2026-10-19
 */
public interface LibUserMapper 
{
    /**
     * 查询图书读者
     * 
     * @param userId 图书读者主键
     * @return 图书读者
     */
    public LibUser selectLibUserByUserId(Long userId);

    /**
     * 查询图书读者列表
     * 
     * @param libUser 图书读者
     * @return 图书读者集合
     */
    public List<LibUser> selectLibUserList(LibUser libUser);

    /**
     * 新增图书读者
     * 
     * @param libUser 图书读者
     * @return 结果
     */
    public int insertLibUser(LibUser libUser);

    /**
     * 修改图书读者
     * 
     * @param libUser 图书读者
     * @return 结果
     */
    public int updateLibUser(LibUser libUser);

    /**
     * 删除图书读者
     * 
     * @param userId 图书读者主键
     * @return 结果
     */
    public int deleteLibUserByUserId(Long userId);

    /**
     * 批量删除图书读者
     * 
     * @param userIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteLibUserByUserIds(Long[] userIds);

    /**
     * 根据系统用户ID查询读者（注册读者绑定）
     * 
     * @param sysUserId 系统用户ID
     * @return 图书读者
     */
    public LibUser selectLibUserBySysUserId(@org.apache.ibatis.annotations.Param("sysUserId") Long sysUserId);

    /** 更新修行字段（功德/魔性/境界） */
    public int updateCultivation(LibUser libUser);

    /** 更新最后签到日期 */
    public int updateLastSignDate(LibUser libUser);

    /** 更新每日奇遇日期 */
    public int updateLastFortuneDate(LibUser libUser);

    /** 增减免罚令数量 */
    public int updateForgiveTokens(@org.apache.ibatis.annotations.Param("userId") Long userId,
                                   @org.apache.ibatis.annotations.Param("delta") int delta);

    /** 更新开仙丹盲盒日期与当日次数 */
    public int updateBoxRecord(@org.apache.ibatis.annotations.Param("userId") Long userId,
                               @org.apache.ibatis.annotations.Param("date") java.util.Date date,
                               @org.apache.ibatis.annotations.Param("count") int count);

    /** 封神榜排行（按功德降序） */
    public List<java.util.Map<String,Object>> selectRankList(@org.apache.ibatis.annotations.Param("limit") int limit);

    /** 冻结长期逾期（逾期超过 days 天仍未还）的读者 */
    public int freezeLongOverdueReaders(@org.apache.ibatis.annotations.Param("days") int days);

    /** 查询某读者仍超期未还（超过 days 天）的借阅数量 */
    public int countLongOverdueByUserId(@org.apache.ibatis.annotations.Param("userId") Long userId,
                                        @org.apache.ibatis.annotations.Param("days") int days);

    /** 解冻读者 */
    public int unfreezeLibUser(@org.apache.ibatis.annotations.Param("userId") Long userId);
}
