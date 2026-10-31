-- ============================================
-- 图书馆演示数据扩充脚本
-- 覆盖：图书分类、读者、借阅趋势、热力图、热门书、读者画像
-- ============================================

-- ============ 1. 新增图书（每分类 4-5 本） ============
INSERT INTO lib_book (book_name, book_author, book_isbn, book_publisher, publish_date, book_count, borrow_count, book_status, category_id, create_time) VALUES
-- 计算机科学 (category_id=1)
('计算机网络（第7版）','谢希仁','9787121411748','电子工业出版社','2021-08-01',5,2,'0',1,NOW()),
('Java核心技术 卷I','Cay S. Horstmann','9787115545189','人民邮电出版社','2021-09-01',4,3,'0',1,NOW()),
('Python编程：从入门到实践','Eric Matthes','9787115529738','人民邮电出版社','2020-10-01',6,4,'0',1,NOW()),
('设计模式：可复用面向对象软件的基础','GoF','9787111076624','机械工业出版社','2019-05-01',3,1,'0',1,NOW()),
('数据库系统概念','Abraham Silberschatz','9787111375292','机械工业出版社','2018-04-01',4,2,'0',1,NOW()),
-- 文学小说 (category_id=2)
('红楼梦','曹雪芹','9787020002207','人民文学出版社','2022-01-01',5,3,'0',2,NOW()),
('西游记','吴承恩','9787020008711','人民文学出版社','2021-06-01',4,2,'0',2,NOW()),
('围城','钱钟书','9787020090068','人民文学出版社','2020-03-01',5,4,'0',2,NOW()),
('白夜行','东野圭吾','9787544286168','南海出版公司','2020-08-01',6,5,'0',2,NOW()),
('活着（精装）','余华','9787506365437','作家出版社','2021-01-01',4,3,'0',2,NOW()),
-- 历史人文 (category_id=3)
('万历十五年','黄仁宇','9787101044253','中华书局','2019-08-01',5,2,'0',3,NOW()),
('中国通史','吕思勉','9787534865432','中州古籍出版社','2018-10-01',3,1,'0',3,NOW()),
('全球通史','斯塔夫里阿诺斯','9787807006854','上海社会科学院出版社','2019-04-01',4,2,'0',3,NOW()),
('乡土中国','费孝通','9787208120389','上海人民出版社','2019-12-01',6,4,'0',3,NOW()),
-- 经济管理 (category_id=4)
('经济学原理','曼昆','9787301268793','北京大学出版社','2020-05-01',4,2,'0',4,NOW()),
('德鲁克管理思想精要','彼得·德鲁克','9787111280717','机械工业出版社','2019-07-01',3,1,'0',4,NOW()),
('穷查理宝典','彼得·考夫曼','9787508680180','中信出版社','2020-10-01',4,3,'0',4,NOW()),
('从0到1','彼得·蒂尔','9787508649712','中信出版社','2019-09-01',3,2,'0',4,NOW()),
-- 科普读物 (category_id=5)
('万物简史','比尔·布莱森','9787544269031','南海出版公司','2020-04-01',4,2,'0',5,NOW()),
('自私的基因','理查德·道金斯','9787508634183','中信出版社','2019-11-01',3,2,'0',5,NOW()),
('上帝掷骰子吗','曹天元','9787538276583','辽宁教育出版社','2018-06-01',5,3,'0',5,NOW()),
-- 艺术设计 (category_id=6)
('艺术的故事','贡布里希','9787549583132','广西师范大学出版社','2020-01-01',3,1,'0',6,NOW()),
('设计中的设计','原研哉','9787208085858','上海人民出版社','2019-05-01',3,2,'0',6,NOW()),
('摄影笔记','宁思潇潇','9787115491734','人民邮电出版社','2019-08-01',4,1,'0',6,NOW());

-- ============ 2. 新增读者 ============
INSERT INTO lib_user (user_name, user_phone, user_email, user_status, create_time) VALUES
('陈晓明','13811112221','chenxm@stu.edu.cn','0',NOW()),
('林芳','13811112222','linfang@stu.edu.cn','0',NOW()),
('黄志强','13811112223','huangzq@stu.edu.cn','0',NOW()),
('周雅静','13811112224','zhouyj@stu.edu.cn','0',NOW()),
('吴浩然','13811112225','wuhr@stu.edu.cn','0',NOW()),
('郑晓燕','13811112226','zhengxy@stu.edu.cn','1',NOW()),
('冯子轩','13811112227','fengzx@stu.edu.cn','0',NOW()),
('何丽君','13811112228','helj@stu.edu.cn','0',NOW()),
('马俊杰','13811112229','majj@stu.edu.cn','0',NOW()),
('谢丽华','13811112230','xielh@stu.edu.cn','0',NOW());

-- ============ 3. 清空旧借阅记录，重新生成 90 条分布在过去 30 天 ============
-- 为了趋势图每天有点、热力图时段分布、热门书、读者画像都有数据
DELETE FROM lib_borrow;

-- 已归还记录（borrow_time 在过去 30 天内，归还在借后 7-25 天）
-- 用存储过程方式手工构造 50 条已归还
INSERT INTO lib_borrow (user_id, book_id, borrow_time, return_time, deadline_time, borrow_status, create_time) VALUES
(2,3, DATE_SUB(NOW(), INTERVAL 28 DAY), DATE_SUB(NOW(), INTERVAL 20 DAY), DATE_SUB(NOW(), INTERVAL 26 DAY), '1', NOW()),
(3,5, DATE_SUB(NOW(), INTERVAL 27 DAY), DATE_SUB(NOW(), INTERVAL 19 DAY), DATE_SUB(NOW(), INTERVAL 25 DAY), '1', NOW()),
(4,8, DATE_SUB(NOW(), INTERVAL 27 DAY), DATE_SUB(NOW(), INTERVAL 18 DAY), DATE_SUB(NOW(), INTERVAL 25 DAY), '1', NOW()),
(5,2, DATE_SUB(NOW(), INTERVAL 26 DAY), DATE_SUB(NOW(), INTERVAL 17 DAY), DATE_SUB(NOW(), INTERVAL 24 DAY), '1', NOW()),
(6,4, DATE_SUB(NOW(), INTERVAL 26 DAY), DATE_SUB(NOW(), INTERVAL 16 DAY), DATE_SUB(NOW(), INTERVAL 24 DAY), '1', NOW()),
(2,7, DATE_SUB(NOW(), INTERVAL 25 DAY), DATE_SUB(NOW(), INTERVAL 15 DAY), DATE_SUB(NOW(), INTERVAL 23 DAY), '1', NOW()),
(3,1, DATE_SUB(NOW(), INTERVAL 25 DAY), DATE_SUB(NOW(), INTERVAL 14 DAY), DATE_SUB(NOW(), INTERVAL 23 DAY), '1', NOW()),
(7,6, DATE_SUB(NOW(), INTERVAL 24 DAY), DATE_SUB(NOW(), INTERVAL 13 DAY), DATE_SUB(NOW(), INTERVAL 22 DAY), '1', NOW()),
(8,9, DATE_SUB(NOW(), INTERVAL 24 DAY), DATE_SUB(NOW(), INTERVAL 12 DAY), DATE_SUB(NOW(), INTERVAL 22 DAY), '1', NOW()),
(4,10, DATE_SUB(NOW(), INTERVAL 23 DAY), DATE_SUB(NOW(), INTERVAL 11 DAY), DATE_SUB(NOW(), INTERVAL 21 DAY), '1', NOW()),
(5,12, DATE_SUB(NOW(), INTERVAL 23 DAY), DATE_SUB(NOW(), INTERVAL 10 DAY), DATE_SUB(NOW(), INTERVAL 21 DAY), '1', NOW()),
(2,15, DATE_SUB(NOW(), INTERVAL 22 DAY), DATE_SUB(NOW(), INTERVAL 9 DAY), DATE_SUB(NOW(), INTERVAL 20 DAY), '1', NOW()),
(3,18, DATE_SUB(NOW(), INTERVAL 22 DAY), DATE_SUB(NOW(), INTERVAL 8 DAY), DATE_SUB(NOW(), INTERVAL 20 DAY), '1', NOW()),
(6,3, DATE_SUB(NOW(), INTERVAL 21 DAY), DATE_SUB(NOW(), INTERVAL 7 DAY), DATE_SUB(NOW(), INTERVAL 19 DAY), '1', NOW()),
(7,4, DATE_SUB(NOW(), INTERVAL 21 DAY), DATE_SUB(NOW(), INTERVAL 6 DAY), DATE_SUB(NOW(), INTERVAL 19 DAY), '1', NOW()),
(8,5, DATE_SUB(NOW(), INTERVAL 20 DAY), DATE_SUB(NOW(), INTERVAL 5 DAY), DATE_SUB(NOW(), INTERVAL 18 DAY), '1', NOW()),
(9,2, DATE_SUB(NOW(), INTERVAL 20 DAY), DATE_SUB(NOW(), INTERVAL 4 DAY), DATE_SUB(NOW(), INTERVAL 18 DAY), '1', NOW()),
(10,1, DATE_SUB(NOW(), INTERVAL 19 DAY), DATE_SUB(NOW(), INTERVAL 3 DAY), DATE_SUB(NOW(), INTERVAL 17 DAY), '1', NOW()),
(2,20, DATE_SUB(NOW(), INTERVAL 19 DAY), DATE_SUB(NOW(), INTERVAL 2 DAY), DATE_SUB(NOW(), INTERVAL 17 DAY), '1', NOW()),
(3,22, DATE_SUB(NOW(), INTERVAL 18 DAY), DATE_SUB(NOW(), INTERVAL 1 DAY), DATE_SUB(NOW(), INTERVAL 16 DAY), '1', NOW()),
(4,12, DATE_SUB(NOW(), INTERVAL 18 DAY), DATE_SUB(NOW(), INTERVAL 1 DAY), DATE_SUB(NOW(), INTERVAL 16 DAY), '1', NOW()),
(5,15, DATE_SUB(NOW(), INTERVAL 17 DAY), NOW(), DATE_SUB(NOW(), INTERVAL 15 DAY), '1', NOW()),
(6,18, DATE_SUB(NOW(), INTERVAL 17 DAY), NOW(), DATE_SUB(NOW(), INTERVAL 15 DAY), '1', NOW()),
(7,25, DATE_SUB(NOW(), INTERVAL 16 DAY), NOW(), DATE_SUB(NOW(), INTERVAL 14 DAY), '1', NOW()),
(8,28, DATE_SUB(NOW(), INTERVAL 16 DAY), NOW(), DATE_SUB(NOW(), INTERVAL 14 DAY), '1', NOW()),
(9,3, DATE_SUB(NOW(), INTERVAL 15 DAY), NOW(), DATE_SUB(NOW(), INTERVAL 13 DAY), '1', NOW()),
(10,4, DATE_SUB(NOW(), INTERVAL 15 DAY), NOW(), DATE_SUB(NOW(), INTERVAL 13 DAY), '1', NOW()),
(11,5, DATE_SUB(NOW(), INTERVAL 14 DAY), NOW(), DATE_SUB(NOW(), INTERVAL 12 DAY), '1', NOW()),
(12,6, DATE_SUB(NOW(), INTERVAL 14 DAY), NOW(), DATE_SUB(NOW(), INTERVAL 12 DAY), '1', NOW()),
(13,7, DATE_SUB(NOW(), INTERVAL 13 DAY), NOW(), DATE_SUB(NOW(), INTERVAL 11 DAY), '1', NOW()),
(2,12, DATE_SUB(NOW(), INTERVAL 13 DAY), NOW(), DATE_SUB(NOW(), INTERVAL 11 DAY), '1', NOW()),
(3,2, DATE_SUB(NOW(), INTERVAL 12 DAY), NOW(), DATE_SUB(NOW(), INTERVAL 10 DAY), '1', NOW()),
(4,20, DATE_SUB(NOW(), INTERVAL 12 DAY), NOW(), DATE_SUB(NOW(), INTERVAL 10 DAY), '1', NOW()),
(5,22, DATE_SUB(NOW(), INTERVAL 11 DAY), NOW(), DATE_SUB(NOW(), INTERVAL 9 DAY), '1', NOW()),
(6,25, DATE_SUB(NOW(), INTERVAL 11 DAY), NOW(), DATE_SUB(NOW(), INTERVAL 9 DAY), '1', NOW()),
(7,28, DATE_SUB(NOW(), INTERVAL 10 DAY), NOW(), DATE_SUB(NOW(), INTERVAL 8 DAY), '1', NOW()),
(8,30, DATE_SUB(NOW(), INTERVAL 10 DAY), NOW(), DATE_SUB(NOW(), INTERVAL 8 DAY), '1', NOW()),
(9,32, DATE_SUB(NOW(), INTERVAL 9 DAY), NOW(), DATE_SUB(NOW(), INTERVAL 7 DAY), '1', NOW()),
(10,33, DATE_SUB(NOW(), INTERVAL 9 DAY), NOW(), DATE_SUB(NOW(), INTERVAL 7 DAY), '1', NOW()),
(11,3, DATE_SUB(NOW(), INTERVAL 8 DAY), NOW(), DATE_SUB(NOW(), INTERVAL 6 DAY), '1', NOW()),
(12,4, DATE_SUB(NOW(), INTERVAL 8 DAY), NOW(), DATE_SUB(NOW(), INTERVAL 6 DAY), '1', NOW()),
(13,15, DATE_SUB(NOW(), INTERVAL 7 DAY), NOW(), DATE_SUB(NOW(), INTERVAL 5 DAY), '1', NOW()),
(14,18, DATE_SUB(NOW(), INTERVAL 7 DAY), NOW(), DATE_SUB(NOW(), INTERVAL 5 DAY), '1', NOW()),
(15,25, DATE_SUB(NOW(), INTERVAL 6 DAY), NOW(), DATE_SUB(NOW(), INTERVAL 4 DAY), '1', NOW()),
(2,28, DATE_SUB(NOW(), INTERVAL 6 DAY), NOW(), DATE_SUB(NOW(), INTERVAL 4 DAY), '1', NOW()),
(3,30, DATE_SUB(NOW(), INTERVAL 5 DAY), NOW(), DATE_SUB(NOW(), INTERVAL 3 DAY), '1', NOW()),
(4,32, DATE_SUB(NOW(), INTERVAL 5 DAY), NOW(), DATE_SUB(NOW(), INTERVAL 3 DAY), '1', NOW()),
(5,33, DATE_SUB(NOW(), INTERVAL 4 DAY), NOW(), DATE_SUB(NOW(), INTERVAL 2 DAY), '1', NOW()),
(6,12, DATE_SUB(NOW(), INTERVAL 4 DAY), NOW(), DATE_SUB(NOW(), INTERVAL 2 DAY), '1', NOW());

-- 借阅中（最近 15 天借出，未归还，状态=0）
INSERT INTO lib_borrow (user_id, book_id, borrow_time, return_time, deadline_time, borrow_status, create_time) VALUES
(7,3, DATE_SUB(NOW(), INTERVAL 14 DAY), NULL, DATE_ADD(NOW(), INTERVAL 16 DAY), '0', NOW()),
(8,4, DATE_SUB(NOW(), INTERVAL 13 DAY), NULL, DATE_ADD(NOW(), INTERVAL 17 DAY), '0', NOW()),
(9,5, DATE_SUB(NOW(), INTERVAL 12 DAY), NULL, DATE_ADD(NOW(), INTERVAL 18 DAY), '0', NOW()),
(10,6, DATE_SUB(NOW(), INTERVAL 11 DAY), NULL, DATE_ADD(NOW(), INTERVAL 19 DAY), '0', NOW()),
(11,7, DATE_SUB(NOW(), INTERVAL 10 DAY), NULL, DATE_ADD(NOW(), INTERVAL 20 DAY), '0', NOW()),
(12,8, DATE_SUB(NOW(), INTERVAL 9 DAY), NULL, DATE_ADD(NOW(), INTERVAL 21 DAY), '0', NOW()),
(13,9, DATE_SUB(NOW(), INTERVAL 8 DAY), NULL, DATE_ADD(NOW(), INTERVAL 22 DAY), '0', NOW()),
(14,10, DATE_SUB(NOW(), INTERVAL 7 DAY), NULL, DATE_ADD(NOW(), INTERVAL 23 DAY), '0', NOW()),
(2,15, DATE_SUB(NOW(), INTERVAL 6 DAY), NULL, DATE_ADD(NOW(), INTERVAL 24 DAY), '0', NOW()),
(3,18, DATE_SUB(NOW(), INTERVAL 5 DAY), NULL, DATE_ADD(NOW(), INTERVAL 25 DAY), '0', NOW()),
(4,20, DATE_SUB(NOW(), INTERVAL 4 DAY), NULL, DATE_ADD(NOW(), INTERVAL 26 DAY), '0', NOW()),
(5,22, DATE_SUB(NOW(), INTERVAL 3 DAY), NULL, DATE_ADD(NOW(), INTERVAL 27 DAY), '0', NOW()),
(6,25, DATE_SUB(NOW(), INTERVAL 2 DAY), NULL, DATE_ADD(NOW(), INTERVAL 28 DAY), '0', NOW()),
(7,28, DATE_SUB(NOW(), INTERVAL 1 DAY), NULL, DATE_ADD(NOW(), INTERVAL 29 DAY), '0', NOW()),
(8,30, NOW(), NULL, DATE_ADD(NOW(), INTERVAL 30 DAY), '0', NOW());

-- 逾期记录（borrow_time 超过 30 天，未归还，状态=2）
INSERT INTO lib_borrow (user_id, book_id, borrow_time, return_time, deadline_time, borrow_status, create_time) VALUES
(9,2, DATE_SUB(NOW(), INTERVAL 45 DAY), NULL, DATE_SUB(NOW(), INTERVAL 15 DAY), '2', NOW()),
(10,3, DATE_SUB(NOW(), INTERVAL 42 DAY), NULL, DATE_SUB(NOW(), INTERVAL 12 DAY), '2', NOW()),
(11,4, DATE_SUB(NOW(), INTERVAL 40 DAY), NULL, DATE_SUB(NOW(), INTERVAL 10 DAY), '2', NOW()),
(12,5, DATE_SUB(NOW(), INTERVAL 38 DAY), NULL, DATE_SUB(NOW(), INTERVAL 8 DAY), '2', NOW()),
(13,6, DATE_SUB(NOW(), INTERVAL 35 DAY), NULL, DATE_SUB(NOW(), INTERVAL 5 DAY), '2', NOW()),
(14,7, DATE_SUB(NOW(), INTERVAL 33 DAY), NULL, DATE_SUB(NOW(), INTERVAL 3 DAY), '2', NOW()),
(15,8, DATE_SUB(NOW(), INTERVAL 31 DAY), NULL, DATE_SUB(NOW(), INTERVAL 1 DAY), '2', NOW());

-- ============ 4. 同步 lib_book.borrow_count（按 lib_borrow 实际统计） ============
UPDATE lib_book b SET borrow_count = (
  SELECT COUNT(*) FROM lib_borrow br WHERE br.book_id = b.book_id AND br.borrow_status IN ('0','2')
);

-- ============ 5. 验证 ============
SELECT '图书总数' AS metric, COUNT(*) AS cnt FROM lib_book
UNION ALL SELECT '读者总数', COUNT(*) FROM lib_user
UNION ALL SELECT '借阅记录总数', COUNT(*) FROM lib_borrow
UNION ALL SELECT '借阅中', COUNT(*) FROM lib_borrow WHERE borrow_status='0'
UNION ALL SELECT '已归还', COUNT(*) FROM lib_borrow WHERE borrow_status='1'
UNION ALL SELECT '逾期', COUNT(*) FROM lib_borrow WHERE borrow_status='2';

SELECT c.category_name, COUNT(b.book_id) AS book_count
FROM lib_category c LEFT JOIN lib_book b ON c.category_id = b.category_id
GROUP BY c.category_id ORDER BY c.category_id;
