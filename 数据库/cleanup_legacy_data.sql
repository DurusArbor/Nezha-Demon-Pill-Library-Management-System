-- ============================================================
-- 哪吒魔丸图书管理系统 · 历史遗留数据清理
-- 适用：ry-vue (MySQL 5.7/8)
-- 1) 删除 7 条 user_id=2 孤儿借阅（均已归还，无库存影响）
-- 2) 历史逾期（埋点前遗留，borrow_status='2'）置为已归还并回补库存，
--    直接改库，不走 doReturn，不补记魔性（与"埋点前不计魔性"逻辑自洽）
-- 3) 清理历史逾期关联的罚金与催缴提醒
-- ============================================================
START TRANSACTION;

-- 1) 孤儿借阅（user_id=2，读者已删除）
DELETE FROM lib_borrow WHERE borrow_id IN (13,18,24,31,43,57,70);

-- 2) 历史逾期置为已归还
UPDATE lib_borrow
SET borrow_status='1', return_time=NOW(), update_time=NOW(),
    remark='历史逾期清理（埋点前遗留数据）'
WHERE borrow_id IN (83,97,98,99,100);

-- 3) 回补库存：每本图书各对应 1 条历史逾期
UPDATE lib_book SET borrow_count = borrow_count - 1
WHERE book_id IN (8,1,2,3,4);

-- 4) 历史逾期关联罚金
DELETE FROM lib_fine WHERE borrow_id IN (83,97,98,99,100);

-- 5) 历史逾期关联催缴提醒
DELETE FROM lib_remind WHERE borrow_id IN (83,97,98,99,100);

COMMIT;
