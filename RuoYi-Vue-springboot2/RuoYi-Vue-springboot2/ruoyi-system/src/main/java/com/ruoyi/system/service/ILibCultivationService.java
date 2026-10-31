package com.ruoyi.system.service;

import java.util.List;
import java.util.Map;

/**
 * 读者修行（魔丸/灵珠信用 + 封神榜境界）Service接口
 *
 * @author ruoyi
 */
public interface ILibCultivationService
{
    /** 改变功德/魔性（写流水 + 更新读者字段） */
    public void change(Long userId, int merit, int demon, String type, String reason);

    /** 每日签到，返回本次获得功德（已签到返回0） */
    public int sign(Long userId);

    /** 每日奇遇/天劫：每天最多一次，20% 概率触发随机事件 */
    public Map<String, Object> fortune(Long userId);

    /** 魔性净化仪式：消耗 100 功德净化 10 点魔性（一念成仙） */
    public Map<String, Object> purify(Long userId);

    /** 开仙丹盲盒：20 功德/次，随机 +10~20 功德，每日最多 3 次，魔丸不可抽 */
    public Map<String, Object> lottery(Long userId);

    /** 兑换免罚令：50 功德/张 */
    public Map<String, Object> buyForgiveToken(Long userId);

    /** 获取读者修行状态（境界、魔丸/灵珠、进度、当前逾期、称号等） */
    public Map<String, Object> getCultivation(Long userId);

    /** 封神榜排行（按功德降序） */
    public List<Map<String, Object>> getRankList(int limit);
}
