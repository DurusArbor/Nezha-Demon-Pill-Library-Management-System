-- ============================================================
-- 哪吒魔丸图书管理系统 · 数据修复 + 公告拆分
-- 1) 陈静重复借阅《百年孤独》(borrow 148) 置已还，同步库存
-- 2) 删除旧公告 17，改为两条：管理端指南 / 读者指南
-- ============================================================
START TRANSACTION;

-- 1) 清理陈静同书重复（保留最早一笔 119，148 置已还）
UPDATE lib_borrow SET borrow_status = '1', return_time = NOW(), update_time = NOW() WHERE borrow_id = 148;
UPDATE lib_book SET borrow_count = borrow_count - 1 WHERE book_id = 6;

-- 2) 删除旧综合公告
DELETE FROM sys_notice WHERE notice_id = 17;

-- 3) 管理端指南公告
INSERT INTO sys_notice (notice_title, notice_type, notice_content, status, create_by, create_time, remark) VALUES
('【管理端指南】系统功能 · 自动化规则与判定口径', '2',
'<div style="font-family:Microsoft YaHei, sans-serif; line-height:1.9; color:#303133;">
<h3 style="margin:6px 0; color:#d4321f;">一、首页数据看板（自动统计）</h3>
<ul>
<li>借阅趋势 / 热门图书 / 逾期率 / 魔丸灵珠分布 / 馆藏册数</li>
<li>口径：馆藏册数 = 全部图书库存总册数；在借 = 未还借阅笔数（借阅中 + 逾期）；逾期率 = 逾期笔数 ÷ 在借笔数</li>
</ul>
<h3 style="margin:6px 0; color:#d4321f;">二、管理功能（手动操作）</h3>
<ul>
<li>图书管理、读者管理、借阅记录、预约管理、罚金管理、逾期处理、公告管理</li>
<li>借阅记录支持书名 + 读者姓名搜索；罚金管理支持读者姓名过滤；逾期处理集中查看</li>
</ul>
<h3 style="margin:6px 0; color:#d4321f;">三、自动执行规则（系统定时/触发）</h3>
<ul>
<li>逾期魔性结算：每逾期一天魔性 +5（登录/扫描时幂等推进），生成罚金时再按罚金金额 ×10 加魔性</li>
<li>罚金生成：逾期还书自动按 0.5 元/天生成未缴罚金</li>
<li>账号冻结：逾期超 3 天自动冻结（读者提示联系管理员），还书后自动解冻</li>
<li>预约到书提醒：预约书到馆后读者登录自动弹窗</li>
<li>罚金催缴提醒：读者登录自动弹窗列出未缴罚金；管理端也可手动发布公告催缴</li>
<li>修行结算：借书 +5 功德；按时还书 +10 且魔性 -2；逾期还书仅 +2；签到 +15；每日奇遇随机 ±</li>
</ul>
<h3 style="margin:6px 0; color:#d4321f;">四、魔丸与灵珠判定口径（全局生效）</h3>
<ul>
<li>灵珠：无当前逾期、无未缴罚金、魔性为 0</li>
<li>魔丸：当前逾期大于 0，或未缴罚金大于 0，或魔性 ≥ 30，任一命中即魔丸</li>
<li>半魔丸：有魔性但未达 30，且无逾期、无罚金</li>
</ul>
<h3 style="margin:6px 0; color:#d4321f;">五、借阅约束</h3>
<ul>
<li>同一读者对同一本书未归还前不可再借；一本书库存借完时自动提示等待归还或预约</li>
<li>借期 30 天、读者上限 5 本、续借 1 次 +15 天</li>
</ul>
</div>',
'0', 'admin', NOW(), '管理端指南公告');

-- 4) 用户端指南公告
INSERT INTO sys_notice (notice_title, notice_type, notice_content, status, create_by, create_time, remark) VALUES
('【读者指南】我的功能 · 魔丸灵珠判定与自动提醒', '2',
'<div style="font-family:Microsoft YaHei, sans-serif; line-height:1.9; color:#303133;">
<h3 style="margin:6px 0; color:#d4321f;">一、我的修行（读者中心）</h3>
<ul>
<li>功德 / 境界 / 称号 / 封神榜；签到手动 +15 功德</li>
<li>每日奇遇：登录自动随机触发，仙丹 +30 功德 或 天劫 -10 功德，每天最多一次</li>
<li>一念成仙：手动净化，每 10 功德洗 1 点魔性，魔丸也能回头</li>
</ul>
<h3 style="margin:6px 0; color:#d4321f;">二、借阅与预约</h3>
<ul>
<li>借期 30 天，最多同时 5 本，可续借 1 次（延长 15 天）</li>
<li>同一本书未归还前不能重复借</li>
<li>预约：书到馆后登录自动弹窗提醒，请及时取书</li>
</ul>
<h3 style="margin:6px 0; color:#d4321f;">三、魔丸与灵珠判定</h3>
<ul>
<li>我是灵珠：无当前逾期、无未缴罚金、魔性为 0</li>
<li>我是魔丸：有当前逾期，或有未缴罚金，或魔性 ≥ 30</li>
<li>变回灵珠：按时还书（魔性 -2）、缴清罚金（收银台一键缴纳）、用功德净化魔性</li>
</ul>
<h3 style="margin:6px 0; color:#d4321f;">四、自动提醒（登录后自动弹窗）</h3>
<ul>
<li>预约到书提醒；未缴罚金催缴提醒；账号冻结提示</li>
</ul>
<h3 style="margin:6px 0; color:#d4321f;">五、冻结规则</h3>
<ul>
<li>逾期超 3 天账号自动冻结，请及时联系管理员；还书后自动解冻</li>
</ul>
<h3 style="margin:6px 0; color:#d4321f;">六、演示账号</h3>
<ul>
<li>读者密码统一 123456，用户名为读者中文名；含 10 位纯净灵珠读者：林清风、苏婉清、顾云舟、白若溪、陆明轩、楚灵薇、沈砚秋、唐婉如、叶千帆、韩疏影</li>
</ul>
</div>',
'0', 'admin', NOW(), '读者指南公告');

COMMIT;
