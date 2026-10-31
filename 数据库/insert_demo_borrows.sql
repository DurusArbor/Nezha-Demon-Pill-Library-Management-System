-- ============================================================
-- 借阅记录演示数据（24 条）：14 按时已归还 + 6 借阅中 + 4 逾期(1~2天)
-- 逾期记录只插借阅，魔性/罚金由系统扫描自动结算（每逾期一天+5 + 罚金金额×10）
-- 逾期天数 1~2 天，不触发「逾期超 3 天自动冻结」
-- 借阅中/逾期 同步 borrow_count +1，已归还不动
-- ============================================================

-- ---------- 一、按时已归还（14 条） ----------
SET @d = DATE_SUB(NOW(), INTERVAL 60 DAY);
INSERT INTO lib_borrow (user_id, book_id, borrow_time, deadline_time, return_time, borrow_status, renew_count, create_time, update_time, create_by, remark)
VALUES (5, 2, DATE_SUB(@d, INTERVAL 30 DAY), @d, DATE_SUB(@d, INTERVAL 3 DAY), '1', 0, NOW(), NOW(), 'admin', '按时还书演示');

SET @d = DATE_SUB(NOW(), INTERVAL 55 DAY);
INSERT INTO lib_borrow (user_id, book_id, borrow_time, deadline_time, return_time, borrow_status, renew_count, create_time, update_time, create_by, remark)
VALUES (6, 9, DATE_SUB(@d, INTERVAL 30 DAY), @d, DATE_SUB(@d, INTERVAL 5 DAY), '1', 0, NOW(), NOW(), 'admin', '按时还书演示');

SET @d = DATE_SUB(NOW(), INTERVAL 50 DAY);
INSERT INTO lib_borrow (user_id, book_id, borrow_time, deadline_time, return_time, borrow_status, renew_count, create_time, update_time, create_by, remark)
VALUES (7, 10, DATE_SUB(@d, INTERVAL 30 DAY), @d, DATE_SUB(@d, INTERVAL 2 DAY), '1', 0, NOW(), NOW(), 'admin', '按时还书演示');

SET @d = DATE_SUB(NOW(), INTERVAL 45 DAY);
INSERT INTO lib_borrow (user_id, book_id, borrow_time, deadline_time, return_time, borrow_status, renew_count, create_time, update_time, create_by, remark)
VALUES (8, 5, DATE_SUB(@d, INTERVAL 30 DAY), @d, DATE_SUB(@d, INTERVAL 4 DAY), '1', 0, NOW(), NOW(), 'admin', '按时还书演示');

SET @d = DATE_SUB(NOW(), INTERVAL 42 DAY);
INSERT INTO lib_borrow (user_id, book_id, borrow_time, deadline_time, return_time, borrow_status, renew_count, create_time, update_time, create_by, remark)
VALUES (9, 12, DATE_SUB(@d, INTERVAL 30 DAY), @d, DATE_SUB(@d, INTERVAL 1 DAY), '1', 0, NOW(), NOW(), 'admin', '按时还书演示');

SET @d = DATE_SUB(NOW(), INTERVAL 40 DAY);
INSERT INTO lib_borrow (user_id, book_id, borrow_time, deadline_time, return_time, borrow_status, renew_count, create_time, update_time, create_by, remark)
VALUES (10, 13, DATE_SUB(@d, INTERVAL 30 DAY), @d, DATE_SUB(@d, INTERVAL 6 DAY), '1', 0, NOW(), NOW(), 'admin', '按时还书演示');

SET @d = DATE_SUB(NOW(), INTERVAL 38 DAY);
INSERT INTO lib_borrow (user_id, book_id, borrow_time, deadline_time, return_time, borrow_status, renew_count, create_time, update_time, create_by, remark)
VALUES (11, 15, DATE_SUB(@d, INTERVAL 30 DAY), @d, DATE_SUB(@d, INTERVAL 3 DAY), '1', 0, NOW(), NOW(), 'admin', '按时还书演示');

SET @d = DATE_SUB(NOW(), INTERVAL 36 DAY);
INSERT INTO lib_borrow (user_id, book_id, borrow_time, deadline_time, return_time, borrow_status, renew_count, create_time, update_time, create_by, remark)
VALUES (12, 18, DATE_SUB(@d, INTERVAL 30 DAY), @d, DATE_SUB(@d, INTERVAL 5 DAY), '1', 0, NOW(), NOW(), 'admin', '按时还书演示');

SET @d = DATE_SUB(NOW(), INTERVAL 34 DAY);
INSERT INTO lib_borrow (user_id, book_id, borrow_time, deadline_time, return_time, borrow_status, renew_count, create_time, update_time, create_by, remark)
VALUES (13, 24, DATE_SUB(@d, INTERVAL 30 DAY), @d, DATE_SUB(@d, INTERVAL 2 DAY), '1', 0, NOW(), NOW(), 'admin', '按时还书演示');

SET @d = DATE_SUB(NOW(), INTERVAL 32 DAY);
INSERT INTO lib_borrow (user_id, book_id, borrow_time, deadline_time, return_time, borrow_status, renew_count, create_time, update_time, create_by, remark)
VALUES (14, 25, DATE_SUB(@d, INTERVAL 30 DAY), @d, DATE_SUB(@d, INTERVAL 4 DAY), '1', 0, NOW(), NOW(), 'admin', '按时还书演示');

SET @d = DATE_SUB(NOW(), INTERVAL 30 DAY);
INSERT INTO lib_borrow (user_id, book_id, borrow_time, deadline_time, return_time, borrow_status, renew_count, create_time, update_time, create_by, remark)
VALUES (15, 27, DATE_SUB(@d, INTERVAL 30 DAY), @d, DATE_SUB(@d, INTERVAL 1 DAY), '1', 0, NOW(), NOW(), 'admin', '按时还书演示');

SET @d = DATE_SUB(NOW(), INTERVAL 28 DAY);
INSERT INTO lib_borrow (user_id, book_id, borrow_time, deadline_time, return_time, borrow_status, renew_count, create_time, update_time, create_by, remark)
VALUES (16, 28, DATE_SUB(@d, INTERVAL 30 DAY), @d, DATE_SUB(@d, INTERVAL 6 DAY), '1', 0, NOW(), NOW(), 'admin', '按时还书演示');

SET @d = DATE_SUB(NOW(), INTERVAL 26 DAY);
INSERT INTO lib_borrow (user_id, book_id, borrow_time, deadline_time, return_time, borrow_status, renew_count, create_time, update_time, create_by, remark)
VALUES (17, 29, DATE_SUB(@d, INTERVAL 30 DAY), @d, DATE_SUB(@d, INTERVAL 3 DAY), '1', 0, NOW(), NOW(), 'admin', '按时还书演示');

SET @d = DATE_SUB(NOW(), INTERVAL 24 DAY);
INSERT INTO lib_borrow (user_id, book_id, borrow_time, deadline_time, return_time, borrow_status, renew_count, create_time, update_time, create_by, remark)
VALUES (18, 30, DATE_SUB(@d, INTERVAL 30 DAY), @d, DATE_SUB(@d, INTERVAL 5 DAY), '1', 0, NOW(), NOW(), 'admin', '按时还书演示');

-- ---------- 二、借阅中（6 条，deadline 未到） ----------
INSERT INTO lib_borrow (user_id, book_id, borrow_time, deadline_time, return_time, borrow_status, renew_count, create_time, update_time, create_by, remark)
VALUES (5, 6, DATE_SUB(NOW(), INTERVAL 10 DAY), DATE_SUB(NOW(), INTERVAL -25 DAY), NULL, '0', 0, NOW(), NOW(), 'admin', '借阅中演示');
UPDATE lib_book SET borrow_count = borrow_count + 1 WHERE book_id = 6;

INSERT INTO lib_borrow (user_id, book_id, borrow_time, deadline_time, return_time, borrow_status, renew_count, create_time, update_time, create_by, remark)
VALUES (13, 7, DATE_SUB(NOW(), INTERVAL 8 DAY), DATE_SUB(NOW(), INTERVAL -22 DAY), NULL, '0', 0, NOW(), NOW(), 'admin', '借阅中演示');
UPDATE lib_book SET borrow_count = borrow_count + 1 WHERE book_id = 7;

INSERT INTO lib_borrow (user_id, book_id, borrow_time, deadline_time, return_time, borrow_status, renew_count, create_time, update_time, create_by, remark)
VALUES (16, 8, DATE_SUB(NOW(), INTERVAL 12 DAY), DATE_SUB(NOW(), INTERVAL -18 DAY), NULL, '0', 0, NOW(), NOW(), 'admin', '借阅中演示');
UPDATE lib_book SET borrow_count = borrow_count + 1 WHERE book_id = 8;

INSERT INTO lib_borrow (user_id, book_id, borrow_time, deadline_time, return_time, borrow_status, renew_count, create_time, update_time, create_by, remark)
VALUES (17, 20, DATE_SUB(NOW(), INTERVAL 6 DAY), DATE_SUB(NOW(), INTERVAL -24 DAY), NULL, '0', 0, NOW(), NOW(), 'admin', '借阅中演示');
UPDATE lib_book SET borrow_count = borrow_count + 1 WHERE book_id = 20;

INSERT INTO lib_borrow (user_id, book_id, borrow_time, deadline_time, return_time, borrow_status, renew_count, create_time, update_time, create_by, remark)
VALUES (14, 26, DATE_SUB(NOW(), INTERVAL 15 DAY), DATE_SUB(NOW(), INTERVAL -15 DAY), NULL, '0', 0, NOW(), NOW(), 'admin', '借阅中演示');
UPDATE lib_book SET borrow_count = borrow_count + 1 WHERE book_id = 26;

INSERT INTO lib_borrow (user_id, book_id, borrow_time, deadline_time, return_time, borrow_status, renew_count, create_time, update_time, create_by, remark)
VALUES (6, 21, DATE_SUB(NOW(), INTERVAL 5 DAY), DATE_SUB(NOW(), INTERVAL -25 DAY), NULL, '0', 0, NOW(), NOW(), 'admin', '借阅中演示');
UPDATE lib_book SET borrow_count = borrow_count + 1 WHERE book_id = 21;

-- ---------- 三、逾期中（4 条，1~2 天，魔性/罚金由扫描自动结算） ----------
INSERT INTO lib_borrow (user_id, book_id, borrow_time, deadline_time, return_time, borrow_status, renew_count, create_time, update_time, create_by, remark)
VALUES (13, 22, DATE_SUB(DATE_SUB(NOW(), INTERVAL 1 DAY), INTERVAL 30 DAY), DATE_SUB(NOW(), INTERVAL 1 DAY), NULL, '2', 0, NOW(), NOW(), 'admin', '逾期演示-1天');
UPDATE lib_book SET borrow_count = borrow_count + 1 WHERE book_id = 22;

INSERT INTO lib_borrow (user_id, book_id, borrow_time, deadline_time, return_time, borrow_status, renew_count, create_time, update_time, create_by, remark)
VALUES (16, 30, DATE_SUB(DATE_SUB(NOW(), INTERVAL 1 DAY), INTERVAL 30 DAY), DATE_SUB(NOW(), INTERVAL 1 DAY), NULL, '2', 0, NOW(), NOW(), 'admin', '逾期演示-1天');
UPDATE lib_book SET borrow_count = borrow_count + 1 WHERE book_id = 30;

INSERT INTO lib_borrow (user_id, book_id, borrow_time, deadline_time, return_time, borrow_status, renew_count, create_time, update_time, create_by, remark)
VALUES (17, 2, DATE_SUB(DATE_SUB(NOW(), INTERVAL 2 DAY), INTERVAL 30 DAY), DATE_SUB(NOW(), INTERVAL 2 DAY), NULL, '2', 0, NOW(), NOW(), 'admin', '逾期演示-2天');
UPDATE lib_book SET borrow_count = borrow_count + 1 WHERE book_id = 2;

INSERT INTO lib_borrow (user_id, book_id, borrow_time, deadline_time, return_time, borrow_status, renew_count, create_time, update_time, create_by, remark)
VALUES (18, 4, DATE_SUB(DATE_SUB(NOW(), INTERVAL 1 DAY), INTERVAL 30 DAY), DATE_SUB(NOW(), INTERVAL 1 DAY), NULL, '2', 0, NOW(), NOW(), 'admin', '逾期演示-1天');
UPDATE lib_book SET borrow_count = borrow_count + 1 WHERE book_id = 4;
