-- ============================================================
-- 哪吒魔丸图书管理系统 · 历史未缴罚金魔性全量补记
-- 规则：罚金金额 × 10（Math.round），写 fine_demon 日志
-- 范围：已归还借阅产生的历史未缴罚金（settle_overdue_demon.sql 只覆盖当前逾期，未覆盖这些）
--      当前逾期关联罚金与新规则生成罚金均已结算，不在此列
-- ============================================================
START TRANSACTION;

-- reader_test1(user_id=25)：fine 16 = 5.50 → 55，fine 17 = 4.50 → 45
UPDATE lib_user SET demon_value = demon_value + 100 WHERE user_id = 25;
-- 赵磊(user_id=6)：fine 25 = 7.50 → 75
UPDATE lib_user SET demon_value = demon_value + 75 WHERE user_id = 6;
-- 冯子轩(user_id=15)：fine 35 = 0.50 → 5
UPDATE lib_user SET demon_value = demon_value + 5 WHERE user_id = 15;

INSERT INTO lib_cultivation_log (user_id, change_type, merit_change, demon_change, reason, create_time) VALUES
(25, 'fine_demon', 0, 55, '历史未缴罚金补记魔性：fine 16 金额 5.50 × 10', NOW()),
(25, 'fine_demon', 0, 45, '历史未缴罚金补记魔性：fine 17 金额 4.50 × 10', NOW()),
(6,  'fine_demon', 0, 75, '历史未缴罚金补记魔性：fine 25 金额 7.50 × 10', NOW()),
(15, 'fine_demon', 0, 5,  '历史未缴罚金补记魔性：fine 35 金额 0.50 × 10', NOW());

COMMIT;
