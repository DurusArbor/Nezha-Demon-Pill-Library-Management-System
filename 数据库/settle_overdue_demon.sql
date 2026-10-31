-- ============================================================
-- 存量逾期魔性一次性补记（规则变更：每逾期一天 +5 + 罚金金额×10）
-- 适用：当前 borrow_status='2' 的逾期未还借阅
-- 说明：已归还的逾期历史按旧规则结算，不回溯重算
-- 执行方式：mysql 命令行 source / 重定向执行
-- ============================================================

-- 1) 聚合每读者应补魔性 = Σ(逾期天数×5 + 已生成罚金金额×10)
CREATE TEMPORARY TABLE tmp_demon AS
SELECT b.user_id,
       SUM(GREATEST(1, DATEDIFF(NOW(), b.deadline_time)) * 5
           + COALESCE(f.fine_amount, 0) * 10) AS delta
FROM lib_borrow b
LEFT JOIN lib_fine f ON f.borrow_id = b.borrow_id
WHERE b.borrow_status = '2'
GROUP BY b.user_id;

-- 2) 写入读者魔性
UPDATE lib_user u JOIN tmp_demon t ON u.user_id = t.user_id
SET u.demon_value = u.demon_value + t.delta;

-- 3) 同步已结算天数（防止后续扫描重复 +5/天）
UPDATE lib_borrow SET demon_settled_days = GREATEST(1, DATEDIFF(NOW(), deadline_time)) WHERE borrow_status = '2';

-- 4) 写修行日志（可审计）
INSERT INTO lib_cultivation_log (user_id, change_type, merit_change, demon_change, reason, create_time)
SELECT user_id, 'overdue_demon', 0, delta, '存量逾期补记（每逾期一天+5 + 罚金金额×10）', NOW() FROM tmp_demon;

DROP TEMPORARY TABLE tmp_demon;
