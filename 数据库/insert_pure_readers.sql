-- ============================================================
-- 哪吒魔丸图书管理系统 · 新增 10 位纯净读者（灵珠）
-- 适用：ry-vue
-- 说明：
--  - 纯净 = 魔性 0、无借阅、无逾期、无罚金（灵珠判定）
--  - 密码统一 123456（复用现有 BCrypt 哈希）
--  - lib_user user_id 32-41，绑定 sys_user user_id 131-140，角色 reader(role_id=100)
-- ============================================================
START TRANSACTION;

INSERT INTO sys_user (user_id, user_name, nick_name, user_type, email, phonenumber, sex, password, status, del_flag, create_by, create_time, remark) VALUES
(131, '林清风', '林清风', '00', 'linqf@example.com',  '13911112231', '0', '$2a$10$5D9s52xiHqRCeuzJkWtl0uLLE1Sdo7Mks/yN5KYmqNqXGO.KxlmES', '0', '0', 'admin', NOW(), '纯净读者演示账号'),
(132, '苏婉清', '苏婉清', '00', 'suwq@example.com',   '13911112232', '0', '$2a$10$5D9s52xiHqRCeuzJkWtl0uLLE1Sdo7Mks/yN5KYmqNqXGO.KxlmES', '0', '0', 'admin', NOW(), '纯净读者演示账号'),
(133, '顾云舟', '顾云舟', '00', 'guyz@example.com',   '13911112233', '0', '$2a$10$5D9s52xiHqRCeuzJkWtl0uLLE1Sdo7Mks/yN5KYmqNqXGO.KxlmES', '0', '0', 'admin', NOW(), '纯净读者演示账号'),
(134, '白若溪', '白若溪', '00', 'bairx@example.com', '13911112234', '0', '$2a$10$5D9s52xiHqRCeuzJkWtl0uLLE1Sdo7Mks/yN5KYmqNqXGO.KxlmES', '0', '0', 'admin', NOW(), '纯净读者演示账号'),
(135, '陆明轩', '陆明轩', '00', 'lumx@example.com',  '13911112235', '0', '$2a$10$5D9s52xiHqRCeuzJkWtl0uLLE1Sdo7Mks/yN5KYmqNqXGO.KxlmES', '0', '0', 'admin', NOW(), '纯净读者演示账号'),
(136, '楚灵薇', '楚灵薇', '00', 'chulw@example.com', '13911112236', '0', '$2a$10$5D9s52xiHqRCeuzJkWtl0uLLE1Sdo7Mks/yN5KYmqNqXGO.KxlmES', '0', '0', 'admin', NOW(), '纯净读者演示账号'),
(137, '沈砚秋', '沈砚秋', '00', 'shenyq@example.com','13911112237', '0', '$2a$10$5D9s52xiHqRCeuzJkWtl0uLLE1Sdo7Mks/yN5KYmqNqXGO.KxlmES', '0', '0', 'admin', NOW(), '纯净读者演示账号'),
(138, '唐婉如', '唐婉如', '00', 'tangwr@example.com','13911112238', '0', '$2a$10$5D9s52xiHqRCeuzJkWtl0uLLE1Sdo7Mks/yN5KYmqNqXGO.KxlmES', '0', '0', 'admin', NOW(), '纯净读者演示账号'),
(139, '叶千帆', '叶千帆', '00', 'yeqf@example.com',  '13911112239', '0', '$2a$10$5D9s52xiHqRCeuzJkWtl0uLLE1Sdo7Mks/yN5KYmqNqXGO.KxlmES', '0', '0', 'admin', NOW(), '纯净读者演示账号'),
(140, '韩疏影', '韩疏影', '00', 'hansy@example.com', '13911112240', '0', '$2a$10$5D9s52xiHqRCeuzJkWtl0uLLE1Sdo7Mks/yN5KYmqNqXGO.KxlmES', '0', '0', 'admin', NOW(), '纯净读者演示账号');

INSERT INTO sys_user_role (user_id, role_id) VALUES
(131, 100), (132, 100), (133, 100), (134, 100), (135, 100),
(136, 100), (137, 100), (138, 100), (139, 100), (140, 100);

INSERT INTO lib_user (user_id, user_name, user_phone, user_email, user_status, merit_points, demon_value, cultivation_level, sys_user_id, create_by, create_time, remark) VALUES
(32, '林清风', '13911112231', 'linqf@example.com',  '0', 0, 0, 0, 131, 'admin', NOW(), '纯净读者（灵珠）'),
(33, '苏婉清', '13911112232', 'suwq@example.com',   '0', 0, 0, 0, 132, 'admin', NOW(), '纯净读者（灵珠）'),
(34, '顾云舟', '13911112233', 'guyz@example.com',   '0', 0, 0, 0, 133, 'admin', NOW(), '纯净读者（灵珠）'),
(35, '白若溪', '13911112234', 'bairx@example.com', '0', 0, 0, 0, 134, 'admin', NOW(), '纯净读者（灵珠）'),
(36, '陆明轩', '13911112235', 'lumx@example.com',  '0', 0, 0, 0, 135, 'admin', NOW(), '纯净读者（灵珠）'),
(37, '楚灵薇', '13911112236', 'chulw@example.com', '0', 0, 0, 0, 136, 'admin', NOW(), '纯净读者（灵珠）'),
(38, '沈砚秋', '13911112237', 'shenyq@example.com','0', 0, 0, 0, 137, 'admin', NOW(), '纯净读者（灵珠）'),
(39, '唐婉如', '13911112238', 'tangwr@example.com','0', 0, 0, 0, 138, 'admin', NOW(), '纯净读者（灵珠）'),
(40, '叶千帆', '13911112239', 'yeqf@example.com',  '0', 0, 0, 0, 139, 'admin', NOW(), '纯净读者（灵珠）'),
(41, '韩疏影', '13911112240', 'hansy@example.com', '0', 0, 0, 0, 140, 'admin', NOW(), '纯净读者（灵珠）');

COMMIT;
