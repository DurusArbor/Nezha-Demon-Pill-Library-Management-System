-- =====================================================================
-- 图书管理系统 · 读者自助端 / 续借罚金 / 预约预借  数据库改动
-- 适用数据库：ry-vue (MySQL 5.7)
-- =====================================================================

-- 1. lib_borrow 增加续借次数
ALTER TABLE lib_borrow ADD COLUMN renew_count int(4) DEFAULT 0 COMMENT '续借次数' AFTER borrow_status;

-- 2. lib_user 增加系统用户ID关联（注册读者绑定 sys_user）
ALTER TABLE lib_user ADD COLUMN sys_user_id bigint(20) DEFAULT NULL COMMENT '关联系统用户ID' AFTER user_status;
ALTER TABLE lib_user ADD UNIQUE KEY uk_lib_user_sys (sys_user_id);

-- 3. 图书预约表
CREATE TABLE IF NOT EXISTS lib_reserve (
  reserve_id bigint(20) NOT NULL AUTO_INCREMENT COMMENT '预约ID',
  user_id bigint(20) NOT NULL COMMENT '读者ID(关联lib_user)',
  book_id bigint(20) NOT NULL COMMENT '图书ID(关联lib_book)',
  reserve_time datetime NOT NULL COMMENT '预约时间',
  expire_time datetime DEFAULT NULL COMMENT '到书保留截止时间',
  reserve_status char(1) DEFAULT '0' COMMENT '状态 0预约中 1已到书待借 2已借出 3已取消 4已过期',
  create_by varchar(64) DEFAULT '' COMMENT '创建者',
  create_time datetime DEFAULT NULL COMMENT '创建时间',
  update_by varchar(64) DEFAULT '' COMMENT '更新者',
  update_time datetime DEFAULT NULL COMMENT '更新时间',
  remark varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (reserve_id),
  KEY idx_reserve_user (user_id),
  KEY idx_reserve_book (book_id),
  KEY idx_reserve_status (reserve_status)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='图书预约表';

-- 4. 借阅罚金表
CREATE TABLE IF NOT EXISTS lib_fine (
  fine_id bigint(20) NOT NULL AUTO_INCREMENT COMMENT '罚金ID',
  borrow_id bigint(20) NOT NULL COMMENT '借阅记录ID(关联lib_borrow)',
  user_id bigint(20) NOT NULL COMMENT '读者ID(关联lib_user)',
  book_id bigint(20) NOT NULL COMMENT '图书ID(关联lib_book)',
  overdue_days int(6) DEFAULT 0 COMMENT '逾期天数',
  fine_amount decimal(10,2) DEFAULT 0.00 COMMENT '罚金金额',
  fine_status char(1) DEFAULT '0' COMMENT '状态 0未缴 1已缴',
  pay_time datetime DEFAULT NULL COMMENT '缴纳时间',
  create_time datetime DEFAULT NULL COMMENT '创建时间',
  remark varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (fine_id),
  KEY idx_fine_user (user_id),
  KEY idx_fine_borrow (borrow_id),
  KEY idx_fine_status (fine_status)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='借阅罚金表';

-- 5. 借阅规则参数（可在 系统管理-参数设置 中修改）
INSERT INTO sys_config (config_name, config_key, config_value, config_type, create_by, create_time, remark) VALUES
('借阅期限（天）', 'lib.borrow.days', '30', 'Y', 'admin', NOW(), '读者每次借阅的默认期限（天）'),
('最大借阅本数', 'lib.borrow.max', '5', 'Y', 'admin', NOW(), '读者最多同时借阅未还的本数'),
('续借次数上限', 'lib.renew.times', '1', 'Y', 'admin', NOW(), '每本图书最多续借次数'),
('续借延长天数', 'lib.renew.days', '15', 'Y', 'admin', NOW(), '每次续借延长的天数'),
('逾期罚金（元/天）', 'lib.fine.perday', '0.5', 'Y', 'admin', NOW(), '逾期每天产生的罚金（元）'),
('预约保留天数', 'lib.reserve.days', '3', 'Y', 'admin', NOW(), '预约到书后保留的天数');

-- 6. 读者角色（注册读者自动分配，用于前端侧边栏按角色显隐）
INSERT INTO sys_role (role_name, role_key, role_sort, data_scope, menu_check_strictly, dept_check_strictly, status, del_flag, create_by, create_time, remark) VALUES
('读者', 'reader', 3, '1', 1, 1, '0', '0', 'admin', NOW(), '自助注册的读者角色');

-- 7. 定时任务：每日扫描逾期并生成罚金（系统监控-定时任务 可查看/手动执行）
INSERT INTO sys_job (job_name, job_group, invoke_target, cron_expression, misfire_policy, concurrent, status, create_by, create_time, remark) VALUES
('逾期罚金扫描', '图书管理', 'libOverdueTask.scanOverdue()', '0 10 0 * * ?', '3', '1', '0', 'admin', NOW(), '每日 00:10 扫描逾期借阅，生成罚金并处理预约过期');

-- 8. 兼容补充：若 lib_fine 已按早期版本建表（缺审计列），执行以下语句补齐
-- ALTER TABLE lib_fine ADD COLUMN create_by varchar(64) DEFAULT '' COMMENT '创建者' AFTER pay_time,
--                     ADD COLUMN update_by varchar(64) DEFAULT '' COMMENT '更新者' AFTER create_by,
--                     ADD COLUMN update_time datetime DEFAULT NULL COMMENT '更新时间' AFTER update_by;
