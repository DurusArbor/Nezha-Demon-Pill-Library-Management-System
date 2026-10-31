-- ============ 1. 重命名部门为图书馆结构 ============
UPDATE sys_dept SET dept_name='图书馆' WHERE dept_id=100;
UPDATE sys_dept SET dept_name='图书馆本部' WHERE dept_id=101;
UPDATE sys_dept SET dept_name='采编部' WHERE dept_id=103;
UPDATE sys_dept SET dept_name='流通部' WHERE dept_id=104;
UPDATE sys_dept SET dept_name='信息技术部' WHERE dept_id=105;
UPDATE sys_dept SET dept_name='读者服务部' WHERE dept_id=106;
UPDATE sys_dept SET dept_name='行政部' WHERE dept_id=107;
UPDATE sys_dept SET dept_name='参考咨询部' WHERE dept_id=108;
UPDATE sys_dept SET dept_name='特藏部' WHERE dept_id=109;

-- ============ 2. 修改 admin 个人信息 ============
UPDATE sys_user SET nick_name='图书管理员', email='lib@library.edu.cn', phonenumber='13900000001'
WHERE user_name='admin';

-- ============ 3. 替换为图书馆公告 ============
UPDATE sys_notice SET
  notice_title='新书到馆通知：120 种新书已上架',
  notice_type='1',
  notice_content='<p>各位读者：</p><p>本批新到图书共 120 种，涵盖计算机、文学、历史、经济等类别，已全部上架至 A 区、B 区书架，欢迎前来借阅。</p><p>其中重点推荐：《深入理解计算机系统（第3版）》《三体》《人类简史》等热门图书。</p>'
WHERE notice_id=1;

UPDATE sys_notice SET
  notice_title='借阅到期提醒',
  notice_type='2',
  notice_content='<p>亲爱的读者：</p><p>您有部分图书即将到期，请尽快归还或办理续借手续。逾期未还将暂停借阅权限，并按每日 0.1 元收取滞纳金。</p><p>如有疑问，请联系流通部服务台。</p>'
WHERE notice_id=2;

UPDATE sys_notice SET
  notice_title='图书馆开放时间调整通知',
  notice_type='1',
  notice_content='<p>自本月起，图书馆开放时间调整如下：</p><p>周一至周日 8:30 - 21:00</p><p>法定节假日开放时间另行通知。</p>'
WHERE notice_id=3;

-- ============ 验证 ============
SELECT notice_id, notice_title, notice_type FROM sys_notice;
SELECT user_id, user_name, nick_name, email, phonenumber FROM sys_user WHERE user_name='admin';
SELECT dept_id, dept_name FROM sys_dept WHERE dept_id IN (100,101,103);
