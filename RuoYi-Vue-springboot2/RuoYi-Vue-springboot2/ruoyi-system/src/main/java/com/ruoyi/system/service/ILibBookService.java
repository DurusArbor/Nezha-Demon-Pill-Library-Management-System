package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.LibBook;

/**
 * 图书信息Service接口
 * 
 * @author ruoyi
 * @date 2026-10-19
 */
public interface ILibBookService 
{
    /**
     * 查询图书信息
     * 
     * @param bookId 图书信息主键
     * @return 图书信息
     */
    public LibBook selectLibBookByBookId(Long bookId);

    /**
     * 查询图书信息列表
     * 
     * @param libBook 图书信息
     * @return 图书信息集合
     */
    public List<LibBook> selectLibBookList(LibBook libBook);

    /**
     * 新增图书信息
     * 
     * @param libBook 图书信息
     * @return 结果
     */
    public int insertLibBook(LibBook libBook);

    /**
     * 修改图书信息
     * 
     * @param libBook 图书信息
     * @return 结果
     */
    public int updateLibBook(LibBook libBook);

    /**
     * 批量删除图书信息
     * 
     * @param bookIds 需要删除的图书信息主键集合
     * @return 结果
     */
    public int deleteLibBookByBookIds(Long[] bookIds);

    /**
     * 删除图书信息信息
     * 
     * @param bookId 图书信息主键
     * @return 结果
     */
    public int deleteLibBookByBookId(Long bookId);
}
