-- ============ 1. 建图书分类表 ============
DROP TABLE IF EXISTS lib_category;
CREATE TABLE lib_category (
  category_id   BIGINT(20)    NOT NULL AUTO_INCREMENT COMMENT '分类ID',
  category_name VARCHAR(50)   NOT NULL COMMENT '分类名称',
  description   VARCHAR(200)  DEFAULT '' COMMENT '描述',
  sort_num      INT(4)        DEFAULT 0 COMMENT '排序',
  create_time   DATETIME      DEFAULT NULL,
  PRIMARY KEY (category_id)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='图书分类表';

-- ============ 2. 插入分类数据 ============
INSERT INTO lib_category (category_name, description, sort_num, create_time) VALUES
('计算机科学', '编程语言、算法、体系结构、软件工程', 1, NOW()),
('文学小说',   '中外文学、小说、散文、诗歌', 2, NOW()),
('历史人文',   '历史、哲学、社会学、人类学', 3, NOW()),
('经济管理',   '经济学、管理学、金融、营销', 4, NOW()),
('科普读物',   '自然科学、科普、传记', 5, NOW()),
('艺术设计',   '绘画、设计、摄影、音乐', 6, NOW());

-- ============ 3. lib_book 加 category_id 字段 ============
ALTER TABLE lib_book ADD COLUMN category_id BIGINT(20) DEFAULT NULL COMMENT '分类ID' AFTER book_status;

-- ============ 4. 现有 10 本图书分配分类 ============
-- 先看下 book_id 对应书名
SELECT book_id, book_name FROM lib_book;
