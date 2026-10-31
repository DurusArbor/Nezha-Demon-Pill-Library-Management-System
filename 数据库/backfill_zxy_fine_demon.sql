-- ============================================================
-- 哪吒魔丸图书管理系统 · 历史罚金魔性补记
-- 郑晓燕(user_id=14) 未缴罚金 fine 6 = 4.50 元，按规则 金额×10 = 45 魔性
-- 背景：该罚金 2026-10-26 生成，早于"罚金落库自动结算魔性"规则，未走结算
-- ============================================================
START TRANSACTION;

UPDATE lib_user SET demon_value = demon_value + 45 WHERE user_id = 14;

INSERT INTO lib_cultivation_log (user_id, change_type, merit_change, demon_change, reason, create_time)
VALUES (14, 'fine_demon', 0, 45, '历史未缴罚金补记魔性：fine 6 金额 4.50 × 10', NOW());

COMMIT;
