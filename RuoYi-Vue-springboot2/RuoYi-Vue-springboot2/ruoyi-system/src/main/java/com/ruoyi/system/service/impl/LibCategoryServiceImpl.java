package com.ruoyi.system.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.LibCategoryMapper;
import com.ruoyi.system.domain.LibCategory;
import com.ruoyi.system.service.ILibCategoryService;

/**
 * 图书分类Service业务层处理
 *
 * @author ruoyi
 */
@Service
public class LibCategoryServiceImpl implements ILibCategoryService
{
    @Autowired
    private LibCategoryMapper libCategoryMapper;

    @Override
    public LibCategory selectLibCategoryByCategoryId(Long categoryId) {
        return libCategoryMapper.selectLibCategoryByCategoryId(categoryId);
    }

    @Override
    public List<LibCategory> selectLibCategoryList(LibCategory libCategory) {
        return libCategoryMapper.selectLibCategoryList(libCategory);
    }

    @Override
    public int insertLibCategory(LibCategory libCategory) {
        libCategory.setCreateTime(DateUtils.getNowDate());
        return libCategoryMapper.insertLibCategory(libCategory);
    }

    @Override
    public int updateLibCategory(LibCategory libCategory) {
        return libCategoryMapper.updateLibCategory(libCategory);
    }

    @Override
    public int deleteLibCategoryByCategoryIds(Long[] categoryIds) {
        return libCategoryMapper.deleteLibCategoryByCategoryIds(categoryIds);
    }

    @Override
    public int deleteLibCategoryByCategoryId(Long categoryId) {
        return libCategoryMapper.deleteLibCategoryByCategoryId(categoryId);
    }
}
