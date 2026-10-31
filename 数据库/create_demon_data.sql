-- 魔丸数据补充脚本
-- 1) 熊安安登录名修复：sys_user 114 原登录名 xx -> 熊安安（与读者姓名一致）
-- 2) 按历史逾期归还记录补建未缴罚金（fine_status='0'，¥0.5/天），使王强/陈静/赵磊成为魔丸
-- 执行方式：mysql -uroot --default-character-set=utf8mb4 < create_demon_data.sql ry-vue

START TRANSACTION;

UPDATE sys_user
SET user_name = '熊安安', nick_name = '熊安安'
WHERE user_id = 114;

-- 王强（user_id=3）：borrow 19 逾期9天 ¥4.50；borrow 32 逾期15天 ¥7.50
INSERT INTO lib_fine (borrow_id, user_id, book_id, overdue_days, fine_amount, fine_status, create_by, create_time)
VALUES (19, 3, 1, 9, 4.50, '0', 'admin', NOW()),
       (32, 3, 22, 15, 7.50, '0', 'admin', NOW());

-- 陈静（user_id=5）：borrow 34 逾期15天 ¥7.50
INSERT INTO lib_fine (borrow_id, user_id, book_id, overdue_days, fine_amount, fine_status, create_by, create_time)
VALUES (34, 5, 15, 15, 7.50, '0', 'admin', NOW());

-- 赵磊（user_id=6）：borrow 35 逾期15天 ¥7.50
INSERT INTO lib_fine (borrow_id, user_id, book_id, overdue_days, fine_amount, fine_status, create_by, create_time)
VALUES (35, 6, 18, 15, 7.50, '0', 'admin', NOW());

COMMIT;
