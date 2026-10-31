-- ============================================================
-- 哪吒魔丸图书管理系统 · 新功能公告
-- 仙丹盲盒 + 免罚令（读者端功能上新）
-- ============================================================
START TRANSACTION;

INSERT INTO sys_notice (notice_title, notice_type, notice_content, status, create_by, create_time, remark) VALUES
('【功能上新】仙丹盲盒 · 免罚令', '2',
'<div style="font-family:Microsoft YaHei, sans-serif; line-height:1.9; color:#303133;">
<h3 style="margin:6px 0; color:#d4321f;">一、开仙丹盲盒（读者中心 · 新）</h3>
<ul>
<li>价格：20 功德/次</li>
<li>奖励：随机 +10 ~ +20 功德，抽到多少全凭仙缘</li>
<li>次数：每日最多 3 次，跨天自动重置</li>
<li>限制：魔丸不可抽——有当前逾期、或未缴罚金、或魔性 ≥ 30 的读者会被拦下，先还书缴罚金或净化魔性再来</li>
</ul>
<h3 style="margin:6px 0; color:#d4321f;">二、免罚令（读者中心 · 新）</h3>
<ul>
<li>价格：50 功德兑换 1 张，长期持有</li>
<li>效果：缴罚金时自动抵消一次，无需支付，收银台会显示本次将自动抵消</li>
<li>用法：在「我的罚金」收银台点击「我已支付」即可，系统自动消耗免罚令并提示</li>
</ul>
<h3 style="margin:6px 0; color:#d4321f;">三、入口与提示</h3>
<ul>
<li>入口：登录后进入读者中心，修行面板即可看到「仙丹盲盒（剩 X）」与「兑换免罚令（持 X）」按钮</li>
<li>功德不足 / 次数用尽 / 魔丸身份均有弹窗提示</li>
<li>盲盒与兑换记录均写入修行日志，可在读者中心查看功德明细</li>
</ul>
</div>',
'0', 'admin', NOW(), '功能上新：仙丹盲盒与免罚令');

COMMIT;
