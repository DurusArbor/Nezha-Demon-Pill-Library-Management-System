-- 逾期处理演示数据：10 条逾期借阅（1~3 天，不触发自动冻结），同步生成未缴罚金并回补借出库存计数
-- 逾期天数故意控制在 1~3 天，避免触发「逾期超 3 天自动冻结」
SET @d = DATE_SUB(NOW(), INTERVAL 1 DAY);
INSERT INTO lib_borrow (user_id, book_id, borrow_time, deadline_time, return_time, borrow_status, renew_count, create_time, update_time, create_by, remark)
VALUES (3, 3, DATE_SUB(@d, INTERVAL 30 DAY), @d, NULL, '2', 0, NOW(), NOW(), 'admin', '逾期演示数据-1天');
SET @b = LAST_INSERT_ID();
INSERT INTO lib_fine (borrow_id, user_id, book_id, overdue_days, fine_amount, fine_status, create_time, update_time, create_by)
VALUES (@b, 3, 3, 1, 0.50, '0', NOW(), NOW(), 'admin');
UPDATE lib_book SET borrow_count = borrow_count + 1 WHERE book_id = 3;

SET @d = DATE_SUB(NOW(), INTERVAL 2 DAY);
INSERT INTO lib_borrow (user_id, book_id, borrow_time, deadline_time, return_time, borrow_status, renew_count, create_time, update_time, create_by, remark)
VALUES (7, 4, DATE_SUB(@d, INTERVAL 30 DAY), @d, NULL, '2', 0, NOW(), NOW(), 'admin', '逾期演示数据-2天');
SET @b = LAST_INSERT_ID();
INSERT INTO lib_fine (borrow_id, user_id, book_id, overdue_days, fine_amount, fine_status, create_time, update_time, create_by)
VALUES (@b, 7, 4, 2, 1.00, '0', NOW(), NOW(), 'admin');
UPDATE lib_book SET borrow_count = borrow_count + 1 WHERE book_id = 4;

SET @d = DATE_SUB(NOW(), INTERVAL 1 DAY);
INSERT INTO lib_borrow (user_id, book_id, borrow_time, deadline_time, return_time, borrow_status, renew_count, create_time, update_time, create_by, remark)
VALUES (8, 14, DATE_SUB(@d, INTERVAL 30 DAY), @d, NULL, '2', 0, NOW(), NOW(), 'admin', '逾期演示数据-1天');
SET @b = LAST_INSERT_ID();
INSERT INTO lib_fine (borrow_id, user_id, book_id, overdue_days, fine_amount, fine_status, create_time, update_time, create_by)
VALUES (@b, 8, 14, 1, 0.50, '0', NOW(), NOW(), 'admin');
UPDATE lib_book SET borrow_count = borrow_count + 1 WHERE book_id = 14;

SET @d = DATE_SUB(NOW(), INTERVAL 3 DAY);
INSERT INTO lib_borrow (user_id, book_id, borrow_time, deadline_time, return_time, borrow_status, renew_count, create_time, update_time, create_by, remark)
VALUES (9, 16, DATE_SUB(@d, INTERVAL 30 DAY), @d, NULL, '2', 0, NOW(), NOW(), 'admin', '逾期演示数据-3天');
SET @b = LAST_INSERT_ID();
INSERT INTO lib_fine (borrow_id, user_id, book_id, overdue_days, fine_amount, fine_status, create_time, update_time, create_by)
VALUES (@b, 9, 16, 3, 1.50, '0', NOW(), NOW(), 'admin');
UPDATE lib_book SET borrow_count = borrow_count + 1 WHERE book_id = 16;

SET @d = DATE_SUB(NOW(), INTERVAL 2 DAY);
INSERT INTO lib_borrow (user_id, book_id, borrow_time, deadline_time, return_time, borrow_status, renew_count, create_time, update_time, create_by, remark)
VALUES (10, 17, DATE_SUB(@d, INTERVAL 30 DAY), @d, NULL, '2', 0, NOW(), NOW(), 'admin', '逾期演示数据-2天');
SET @b = LAST_INSERT_ID();
INSERT INTO lib_fine (borrow_id, user_id, book_id, overdue_days, fine_amount, fine_status, create_time, update_time, create_by)
VALUES (@b, 10, 17, 2, 1.00, '0', NOW(), NOW(), 'admin');
UPDATE lib_book SET borrow_count = borrow_count + 1 WHERE book_id = 17;

SET @d = DATE_SUB(NOW(), INTERVAL 1 DAY);
INSERT INTO lib_borrow (user_id, book_id, borrow_time, deadline_time, return_time, borrow_status, renew_count, create_time, update_time, create_by, remark)
VALUES (11, 23, DATE_SUB(@d, INTERVAL 30 DAY), @d, NULL, '2', 0, NOW(), NOW(), 'admin', '逾期演示数据-1天');
SET @b = LAST_INSERT_ID();
INSERT INTO lib_fine (borrow_id, user_id, book_id, overdue_days, fine_amount, fine_status, create_time, update_time, create_by)
VALUES (@b, 11, 23, 1, 0.50, '0', NOW(), NOW(), 'admin');
UPDATE lib_book SET borrow_count = borrow_count + 1 WHERE book_id = 23;

SET @d = DATE_SUB(NOW(), INTERVAL 3 DAY);
INSERT INTO lib_borrow (user_id, book_id, borrow_time, deadline_time, return_time, borrow_status, renew_count, create_time, update_time, create_by, remark)
VALUES (12, 19, DATE_SUB(@d, INTERVAL 30 DAY), @d, NULL, '2', 0, NOW(), NOW(), 'admin', '逾期演示数据-3天');
SET @b = LAST_INSERT_ID();
INSERT INTO lib_fine (borrow_id, user_id, book_id, overdue_days, fine_amount, fine_status, create_time, update_time, create_by)
VALUES (@b, 12, 19, 3, 1.50, '0', NOW(), NOW(), 'admin');
UPDATE lib_book SET borrow_count = borrow_count + 1 WHERE book_id = 19;

SET @d = DATE_SUB(NOW(), INTERVAL 2 DAY);
INSERT INTO lib_borrow (user_id, book_id, borrow_time, deadline_time, return_time, borrow_status, renew_count, create_time, update_time, create_by, remark)
VALUES (18, 31, DATE_SUB(@d, INTERVAL 30 DAY), @d, NULL, '2', 0, NOW(), NOW(), 'admin', '逾期演示数据-2天');
SET @b = LAST_INSERT_ID();
INSERT INTO lib_fine (borrow_id, user_id, book_id, overdue_days, fine_amount, fine_status, create_time, update_time, create_by)
VALUES (@b, 18, 31, 2, 1.00, '0', NOW(), NOW(), 'admin');
UPDATE lib_book SET borrow_count = borrow_count + 1 WHERE book_id = 31;

SET @d = DATE_SUB(NOW(), INTERVAL 1 DAY);
INSERT INTO lib_borrow (user_id, book_id, borrow_time, deadline_time, return_time, borrow_status, renew_count, create_time, update_time, create_by, remark)
VALUES (15, 32, DATE_SUB(@d, INTERVAL 30 DAY), @d, NULL, '2', 0, NOW(), NOW(), 'admin', '逾期演示数据-1天');
SET @b = LAST_INSERT_ID();
INSERT INTO lib_fine (borrow_id, user_id, book_id, overdue_days, fine_amount, fine_status, create_time, update_time, create_by)
VALUES (@b, 15, 32, 1, 0.50, '0', NOW(), NOW(), 'admin');
UPDATE lib_book SET borrow_count = borrow_count + 1 WHERE book_id = 32;

SET @d = DATE_SUB(NOW(), INTERVAL 3 DAY);
INSERT INTO lib_borrow (user_id, book_id, borrow_time, deadline_time, return_time, borrow_status, renew_count, create_time, update_time, create_by, remark)
VALUES (19, 36, DATE_SUB(@d, INTERVAL 30 DAY), @d, NULL, '2', 0, NOW(), NOW(), 'admin', '逾期演示数据-3天');
SET @b = LAST_INSERT_ID();
INSERT INTO lib_fine (borrow_id, user_id, book_id, overdue_days, fine_amount, fine_status, create_time, update_time, create_by)
VALUES (@b, 19, 36, 3, 1.50, '0', NOW(), NOW(), 'admin');
UPDATE lib_book SET borrow_count = borrow_count + 1 WHERE book_id = 36;
