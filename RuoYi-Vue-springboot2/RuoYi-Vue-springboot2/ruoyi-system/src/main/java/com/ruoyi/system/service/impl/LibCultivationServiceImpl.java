package com.ruoyi.system.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.system.domain.LibCultivationLog;
import com.ruoyi.system.domain.LibUser;
import com.ruoyi.system.mapper.LibBorrowMapper;
import com.ruoyi.system.mapper.LibCultivationLogMapper;
import com.ruoyi.system.mapper.LibFineMapper;
import com.ruoyi.system.mapper.LibUserMapper;
import com.ruoyi.system.service.ILibCultivationService;

/**
 * 读者修行（魔丸/灵珠信用 + 封神榜境界）Service实现
 *
 * @author ruoyi
 */
@Service
public class LibCultivationServiceImpl implements ILibCultivationService
{
    /** 境界阈值 */
    private static final int[] LEVEL_THRESHOLDS = { 0, 100, 300, 600, 1200, 2500 };
    private static final String[] LEVEL_NAMES =
        { "凡人", "道童", "天兵", "仙将", "三坛海会大神", "魔童战神" };
    /** 魔丸判定的魔性阈值 */
    private static final int DEMON_HARD = 30;

    /** 每日奇遇触发概率 */
    private static final double FORTUNE_RATE = 0.2;

    private static final Random RAND = new Random();

    @Autowired
    private LibUserMapper userMapper;

    @Autowired
    private LibCultivationLogMapper logMapper;

    @Autowired
    private LibBorrowMapper borrowMapper;

    @Autowired
    private LibFineMapper fineMapper;

    /** 截断到当天 00:00:00 */
    private Date truncateDate(Date d) {
        Calendar c = Calendar.getInstance();
        c.setTime(d);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        return c.getTime();
    }

    /** 根据功德计算境界 */
    private int calcLevel(int merit) {
        int lv = 0;
        for (int i = 0; i < LEVEL_THRESHOLDS.length; i++) {
            if (merit >= LEVEL_THRESHOLDS[i]) {
                lv = i;
            }
        }
        return lv;
    }

    @Override
    public void change(Long userId, int merit, int demon, String type, String reason) {
        LibUser user = userMapper.selectLibUserByUserId(userId);
        if (user == null) {
            return;
        }
        int curMerit = user.getMeritPoints() == null ? 0 : user.getMeritPoints();
        int curDemon = user.getDemonValue() == null ? 0 : user.getDemonValue();
        int newMerit = Math.max(0, curMerit + merit);
        int newDemon = Math.max(0, curDemon + demon);
        int newLevel = calcLevel(newMerit);

        LibUser upd = new LibUser();
        upd.setUserId(userId);
        upd.setMeritPoints(newMerit);
        upd.setDemonValue(newDemon);
        upd.setCultivationLevel(newLevel);
        userMapper.updateCultivation(upd);

        LibCultivationLog log = new LibCultivationLog();
        log.setUserId(userId);
        log.setChangeType(type);
        log.setMeritChange(merit);
        log.setDemonChange(demon);
        log.setReason(reason);
        log.setCreateTime(new Date());
        logMapper.insertLibCultivationLog(log);
    }

    @Override
    public int sign(Long userId) {
        LibUser user = userMapper.selectLibUserByUserId(userId);
        if (user == null) {
            throw new ServiceException("读者不存在");
        }
        Date today = truncateDate(new Date());
        if (user.getLastSignDate() != null
                && truncateDate(user.getLastSignDate()).compareTo(today) >= 0) {
            return 0; // 今日已签到
        }
        LibUser upd = new LibUser();
        upd.setUserId(userId);
        upd.setLastSignDate(new Date());
        userMapper.updateLastSignDate(upd);

        // 每日签到固定功德 +15
        int gain = 15;
        change(userId, gain, 0, "sign", "每日签到，功德 +15");
        return gain;
    }

    @Override
    public Map<String, Object> fortune(Long userId) {
        LibUser user = userMapper.selectLibUserByUserId(userId);
        if (user == null) {
            throw new ServiceException("读者不存在");
        }
        Date today = truncateDate(new Date());
        // 每日最多一次奇遇
        if (user.getLastFortuneDate() != null
                && truncateDate(user.getLastFortuneDate()).compareTo(today) >= 0) {
            Map<String, Object> r = new HashMap<>();
            r.put("triggered", false);
            r.put("message", "今日奇遇已过，明日再来");
            return r;
        }
        LibUser upd = new LibUser();
        upd.setUserId(userId);
        upd.setLastFortuneDate(new Date());
        userMapper.updateLastFortuneDate(upd);

        Map<String, Object> r = new HashMap<>();
        if (RAND.nextDouble() < FORTUNE_RATE) {
            // 50% 仙丹 / 50% 天劫
            boolean good = RAND.nextBoolean();
            if (good) {
                change(userId, 30, 0, "fortune", "每日奇遇：捡到仙丹，功德 +30");
                r.put("triggered", true);
                r.put("good", true);
                r.put("message", "奇遇降临！捡到仙丹，功德 +30");
            } else {
                change(userId, -10, 0, "fortune", "每日奇遇：遭遇天劫，功德 -10");
                r.put("triggered", true);
                r.put("good", false);
                r.put("message", "天劫突至！遭遇天劫，功德 -10");
            }
        } else {
            r.put("triggered", false);
            r.put("message", "今日平淡无奇，未遇奇遇");
        }
        return r;
    }

    @Override
    public Map<String, Object> purify(Long userId) {
        LibUser user = userMapper.selectLibUserByUserId(userId);
        if (user == null) {
            throw new ServiceException("读者不存在");
        }
        int merit = user.getMeritPoints() == null ? 0 : user.getMeritPoints();
        int demon = user.getDemonValue() == null ? 0 : user.getDemonValue();
        if (demon <= 0) {
            throw new ServiceException("魔性已清零，无需净化");
        }
        if (merit < 10) {
            throw new ServiceException("功德不足，每 10 功德可净化 1 点魔性");
        }
        // 比例制：10 功德净化 1 点魔性，最多洗到魔性清零
        int clean = Math.min(demon, merit / 10);
        if (clean <= 0) {
            throw new ServiceException("功德不足，每 10 功德可净化 1 点魔性");
        }
        int cost = clean * 10;
        change(userId, -cost, -clean, "purify", "一念成仙：消耗 " + cost + " 功德净化 " + clean + " 点魔性");
        LibUser after = userMapper.selectLibUserByUserId(userId);
        Map<String, Object> r = new HashMap<>();
        r.put("meritPoints", after.getMeritPoints());
        r.put("demonValue", after.getDemonValue());
        r.put("level", after.getCultivationLevel());
        r.put("cleanPoints", clean);
        r.put("costMerit", cost);
        return r;
    }

    @Override
    public Map<String, Object> lottery(Long userId) {
        LibUser user = userMapper.selectLibUserByUserId(userId);
        if (user == null) {
            throw new ServiceException("读者不存在");
        }
        int demon = user.getDemonValue() == null ? 0 : user.getDemonValue();
        int overdue = borrowMapper.countOverdueByUserId(userId);
        int unpaid = fineMapper.countUnpaidByUserId(userId);
        if (overdue > 0 || unpaid > 0 || demon >= DEMON_HARD) {
            throw new ServiceException("魔丸不可开仙丹盲盒，先还清逾期、缴清罚金或净化魔性");
        }
        int merit = user.getMeritPoints() == null ? 0 : user.getMeritPoints();
        if (merit < 20) {
            throw new ServiceException("功德不足，开仙丹盲盒需 20 功德");
        }
        // 每日最多 3 次（按自然日）
        Date today = truncateDate(new Date());
        int used = 0;
        Date boxDate = user.getBoxDate();
        if (boxDate != null && boxDate.equals(today)) {
            used = user.getBoxCountToday() == null ? 0 : user.getBoxCountToday();
        }
        if (used >= 3) {
            throw new ServiceException("今日已开 3 次仙丹盲盒，明日再来");
        }
        int gained = 10 + RAND.nextInt(191); // 10 ~ 200
        change(userId, -20, 0, "lottery_cost", "开仙丹盲盒消耗功德 20");
        change(userId, gained, 0, "lottery", "仙丹盲盒开出仙丹，功德 +" + gained);
        userMapper.updateBoxRecord(userId, today, used + 1);
        LibUser after = userMapper.selectLibUserByUserId(userId);
        Map<String, Object> r = new HashMap<>();
        r.put("gained", gained);
        r.put("boxUsedToday", used + 1);
        r.put("boxLeftToday", 3 - used - 1);
        r.put("meritPoints", after.getMeritPoints());
        r.put("level", after.getCultivationLevel());
        return r;
    }

    @Override
    public Map<String, Object> buyForgiveToken(Long userId) {
        LibUser user = userMapper.selectLibUserByUserId(userId);
        if (user == null) {
            throw new ServiceException("读者不存在");
        }
        int merit = user.getMeritPoints() == null ? 0 : user.getMeritPoints();
        if (merit < 50) {
            throw new ServiceException("功德不足，兑换免罚令需 50 功德");
        }
        change(userId, -50, 0, "token_buy", "兑换免罚令消耗功德 50");
        userMapper.updateForgiveTokens(userId, 1);
        LibUser after = userMapper.selectLibUserByUserId(userId);
        Map<String, Object> r = new HashMap<>();
        r.put("tokens", after.getForgiveTokens());
        r.put("meritPoints", after.getMeritPoints());
        r.put("level", after.getCultivationLevel());
        return r;
    }

    @Override
    public Map<String, Object> getCultivation(Long userId) {
        LibUser user = userMapper.selectLibUserByUserId(userId);
        if (user == null) {
            throw new ServiceException("读者不存在");
        }
        int merit = user.getMeritPoints() == null ? 0 : user.getMeritPoints();
        int demon = user.getDemonValue() == null ? 0 : user.getDemonValue();
        int level = calcLevel(merit);
        int overdueCount = borrowMapper.countOverdueByUserId(userId);
        int unpaidFineCount = fineMapper.countUnpaidByUserId(userId);

        // 魔丸 / 灵珠（当前逾期、未缴罚金或魔性≥30 即魔丸；否则灵珠，魔性<30 也算灵珠）
        String nature;
        if (overdueCount > 0 || unpaidFineCount > 0 || demon >= DEMON_HARD) {
            nature = "demon";
        } else {
            nature = "spirit";
        }

        Map<String, Object> m = new HashMap<>();
        m.put("userId", userId);
        m.put("userName", user.getUserName());
        m.put("meritPoints", merit);
        m.put("demonValue", demon);
        m.put("level", level);
        m.put("levelName", LEVEL_NAMES[level]);
        m.put("nature", nature);
        m.put("overdueCount", overdueCount);
        m.put("unpaidFineCount", unpaidFineCount);

        // 称号：按行为累计解锁（借阅狂魔/按时还书模范/藏书仙人/罚单大户）
        int totalBorrows = borrowMapper.countTotalByUserId(userId);
        int onTimeReturns = borrowMapper.countOnTimeReturnByUserId(userId);
        int totalFines = fineMapper.countFineByUserId(userId);
        List<String> titles = new ArrayList<>();
        if (totalBorrows >= 10) {
            titles.add("借阅狂魔");
        }
        if (onTimeReturns >= 2) {
            titles.add("按时还书模范");
        }
        if (totalBorrows >= 15) {
            titles.add("藏书仙人");
        }
        if (totalFines >= 3) {
            titles.add("罚单大户");
        }
        m.put("titles", titles);
        boolean signedToday = user.getLastSignDate() != null
                && truncateDate(user.getLastSignDate()).compareTo(truncateDate(new Date())) >= 0;
        m.put("signedToday", signedToday);

        // 免罚令与仙丹盲盒状态
        int forgiveTokens = user.getForgiveTokens() == null ? 0 : user.getForgiveTokens();
        m.put("forgiveTokens", forgiveTokens);
        Date today = truncateDate(new Date());
        int boxUsed = 0;
        Date boxDate = user.getBoxDate();
        if (boxDate != null && boxDate.equals(today)) {
            boxUsed = user.getBoxCountToday() == null ? 0 : user.getBoxCountToday();
        }
        m.put("boxUsedToday", boxUsed);
        m.put("boxLeftToday", Math.max(0, 3 - boxUsed));

        if (level < LEVEL_THRESHOLDS.length - 1) {
            int cur = merit - LEVEL_THRESHOLDS[level];
            int span = LEVEL_THRESHOLDS[level + 1] - LEVEL_THRESHOLDS[level];
            m.put("curProgress", cur);
            m.put("spanProgress", span);
            m.put("nextLevelName", LEVEL_NAMES[level + 1]);
            m.put("percent", Math.min(100, Math.round(cur * 100.0 / span)));
        } else {
            m.put("curProgress", 0);
            m.put("spanProgress", 1);
            m.put("nextLevelName", "已圆满");
            m.put("percent", 100);
        }
        return m;
    }

    @Override
    public List<Map<String, Object>> getRankList(int limit) {
        return userMapper.selectRankList(limit);
    }
}
