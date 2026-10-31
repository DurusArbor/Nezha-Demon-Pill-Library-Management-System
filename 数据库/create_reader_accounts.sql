-- ============================================================
-- 哪吒魔丸图书管理系统 · 为无登录账号的读者补建登录账号
-- 适用：ry-vue
-- 说明：
--  - sys_user.user_name 即登录名，取 lib_user.user_name（中文可登录）
--  - 密码统一为 123456（与 sys.user.initPassword 一致，复用现有 BCrypt 哈希）
--  - 角色绑定 reader(role_id=100)，回填 lib_user.sys_user_id
--  - 郑晓燕(user_status=1)账号一并建立，但读者自助端会被"已冻结"拦截
-- ============================================================
START TRANSACTION;

INSERT INTO sys_user (user_id, user_name, nick_name, user_type, email, phonenumber, sex, password, status, del_flag, create_by, create_time, remark) VALUES
(115, '王强',   '王强',   '00', 'wangqiang@example.com', '13700034567', '0', '$2a$10$5D9s52xiHqRCeuzJkWtl0uLLE1Sdo7Mks/yN5KYmqNqXGO.KxlmES', '0', '0', 'admin', NOW(), '批量补建读者登录账号'),
(116, '陈静',   '陈静',   '00', 'chenjing@example.com',  '13500056789', '0', '$2a$10$5D9s52xiHqRCeuzJkWtl0uLLE1Sdo7Mks/yN5KYmqNqXGO.KxlmES', '0', '0', 'admin', NOW(), '批量补建读者登录账号'),
(117, '赵磊',   '赵磊',   '00', 'zhaolei@example.com',   '13300067890', '0', '$2a$10$5D9s52xiHqRCeuzJkWtl0uLLE1Sdo7Mks/yN5KYmqNqXGO.KxlmES', '0', '0', 'admin', NOW(), '批量补建读者登录账号'),
(118, '孙丽',   '孙丽',   '00', 'sunli@example.com',     '13100078901', '0', '$2a$10$5D9s52xiHqRCeuzJkWtl0uLLE1Sdo7Mks/yN5KYmqNqXGO.KxlmES', '0', '0', 'admin', NOW(), '批量补建读者登录账号'),
(119, '周敏',   '周敏',   '00', 'zhoumin@example.com',   '13200089012', '0', '$2a$10$5D9s52xiHqRCeuzJkWtl0uLLE1Sdo7Mks/yN5KYmqNqXGO.KxlmES', '0', '0', 'admin', NOW(), '批量补建读者登录账号'),
(120, '陈晓明', '陈晓明', '00', 'chenxm@stu.edu.cn',     '13811112221', '0', '$2a$10$5D9s52xiHqRCeuzJkWtl0uLLE1Sdo7Mks/yN5KYmqNqXGO.KxlmES', '0', '0', 'admin', NOW(), '批量补建读者登录账号'),
(121, '林芳',   '林芳',   '00', 'linfang@stu.edu.cn',    '13811112222', '0', '$2a$10$5D9s52xiHqRCeuzJkWtl0uLLE1Sdo7Mks/yN5KYmqNqXGO.KxlmES', '0', '0', 'admin', NOW(), '批量补建读者登录账号'),
(122, '黄志强', '黄志强', '00', 'huangzq@stu.edu.cn',    '13811112223', '0', '$2a$10$5D9s52xiHqRCeuzJkWtl0uLLE1Sdo7Mks/yN5KYmqNqXGO.KxlmES', '0', '0', 'admin', NOW(), '批量补建读者登录账号'),
(123, '周雅静', '周雅静', '00', 'zhouyj@stu.edu.cn',     '13811112224', '0', '$2a$10$5D9s52xiHqRCeuzJkWtl0uLLE1Sdo7Mks/yN5KYmqNqXGO.KxlmES', '0', '0', 'admin', NOW(), '批量补建读者登录账号'),
(124, '吴浩然', '吴浩然', '00', 'wuhr@stu.edu.cn',       '13811112225', '0', '$2a$10$5D9s52xiHqRCeuzJkWtl0uLLE1Sdo7Mks/yN5KYmqNqXGO.KxlmES', '0', '0', 'admin', NOW(), '批量补建读者登录账号'),
(125, '郑晓燕', '郑晓燕', '00', 'zhengxy@stu.edu.cn',    '13811112226', '0', '$2a$10$5D9s52xiHqRCeuzJkWtl0uLLE1Sdo7Mks/yN5KYmqNqXGO.KxlmES', '0', '0', 'admin', NOW(), '批量补建读者登录账号（读者端已冻结）'),
(126, '冯子轩', '冯子轩', '00', 'fengzx@stu.edu.cn',     '13811112227', '0', '$2a$10$5D9s52xiHqRCeuzJkWtl0uLLE1Sdo7Mks/yN5KYmqNqXGO.KxlmES', '0', '0', 'admin', NOW(), '批量补建读者登录账号'),
(127, '何丽君', '何丽君', '00', 'helj@stu.edu.cn',       '13811112228', '0', '$2a$10$5D9s52xiHqRCeuzJkWtl0uLLE1Sdo7Mks/yN5KYmqNqXGO.KxlmES', '0', '0', 'admin', NOW(), '批量补建读者登录账号'),
(128, '马俊杰', '马俊杰', '00', 'majj@stu.edu.cn',       '13811112229', '0', '$2a$10$5D9s52xiHqRCeuzJkWtl0uLLE1Sdo7Mks/yN5KYmqNqXGO.KxlmES', '0', '0', 'admin', NOW(), '批量补建读者登录账号'),
(129, '谢丽华', '谢丽华', '00', 'xielh@stu.edu.cn',      '13811112230', '0', '$2a$10$5D9s52xiHqRCeuzJkWtl0uLLE1Sdo7Mks/yN5KYmqNqXGO.KxlmES', '0', '0', 'admin', NOW(), '批量补建读者登录账号'),
(130, '小南',   '小南',   '00', '',                      '',            '0', '$2a$10$5D9s52xiHqRCeuzJkWtl0uLLE1Sdo7Mks/yN5KYmqNqXGO.KxlmES', '0', '0', 'admin', NOW(), '批量补建读者登录账号');

INSERT INTO sys_user_role (user_id, role_id) VALUES
(115, 100), (116, 100), (117, 100), (118, 100), (119, 100),
(120, 100), (121, 100), (122, 100), (123, 100), (124, 100),
(125, 100), (126, 100), (127, 100), (128, 100), (129, 100), (130, 100);

UPDATE lib_user SET sys_user_id = 115 WHERE user_id = 3;
UPDATE lib_user SET sys_user_id = 116 WHERE user_id = 5;
UPDATE lib_user SET sys_user_id = 117 WHERE user_id = 6;
UPDATE lib_user SET sys_user_id = 118 WHERE user_id = 7;
UPDATE lib_user SET sys_user_id = 119 WHERE user_id = 8;
UPDATE lib_user SET sys_user_id = 120 WHERE user_id = 9;
UPDATE lib_user SET sys_user_id = 121 WHERE user_id = 10;
UPDATE lib_user SET sys_user_id = 122 WHERE user_id = 11;
UPDATE lib_user SET sys_user_id = 123 WHERE user_id = 12;
UPDATE lib_user SET sys_user_id = 124 WHERE user_id = 13;
UPDATE lib_user SET sys_user_id = 125 WHERE user_id = 14;
UPDATE lib_user SET sys_user_id = 126 WHERE user_id = 15;
UPDATE lib_user SET sys_user_id = 127 WHERE user_id = 16;
UPDATE lib_user SET sys_user_id = 128 WHERE user_id = 17;
UPDATE lib_user SET sys_user_id = 129 WHERE user_id = 18;
UPDATE lib_user SET sys_user_id = 130 WHERE user_id = 19;

COMMIT;
