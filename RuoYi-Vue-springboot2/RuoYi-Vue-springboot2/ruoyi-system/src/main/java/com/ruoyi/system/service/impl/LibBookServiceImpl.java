package com.ruoyi.system.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.LibBookMapper;
import com.ruoyi.system.domain.LibBook;
import com.ruoyi.system.service.ILibBookService;

/**
 * 图书信息Service业务层处理
 * 
 * @author ruoyi
 * @date 2026-10-19
 */
@Service
public class LibBookServiceImpl implements ILibBookService 
{
    @Autowired
    private LibBookMapper libBookMapper;

    /**
     * 查询图书信息
     * 
     * @param bookId 图书信息主键
     * @return 图书信息
     */
    @Override
    public LibBook selectLibBookByBookId(Long bookId)
    {
        return libBookMapper.selectLibBookByBookId(bookId);
    }

    /**
     * 查询图书信息列表
     * 
     * @param libBook 图书信息
     * @return 图书信息
     */
    @Override
    public List<LibBook> selectLibBookList(LibBook libBook)
    {
        return libBookMapper.selectLibBookList(libBook);
    }

    /**
     * 新增图书信息
     * 
     * @param libBook 图书信息
     * @return 结果
     */
    @Override
    public int insertLibBook(LibBook libBook)
    {
        libBook.setCreateTime(DateUtils.getNowDate());
        return libBookMapper.insertLibBook(libBook);
    }

    /**
     * 修改图书信息
     * 
     * @param libBook 图书信息
     * @return 结果
     */
    @Override
    public int updateLibBook(LibBook libBook)
    {
        libBook.setUpdateTime(DateUtils.getNowDate());
        return libBookMapper.updateLibBook(libBook);
    }

    /**
     * 批量删除图书信息
     * 
     * @param bookIds 需要删除的图书信息主键
     * @return 结果
     */
    @Override
    public int deleteLibBookByBookIds(Long[] bookIds)
    {
        return libBookMapper.deleteLibBookByBookIds(bookIds);
    }

    /**
     * 删除图书信息信息
     * 
     * @param bookId 图书信息主键
     * @return 结果
     */
    @Override
    public int deleteLibBookByBookId(Long bookId)
    {
        return libBookMapper.deleteLibBookByBookId(bookId);
    }
}
