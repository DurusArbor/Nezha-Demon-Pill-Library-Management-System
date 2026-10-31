package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.LibCategory;

/**
 * 图书分类Service接口
 *
 * @author ruoyi
 */
public interface ILibCategoryService
{
    public LibCategory selectLibCategoryByCategoryId(Long categoryId);
    public List<LibCategory> selectLibCategoryList(LibCategory libCategory);
    public int insertLibCategory(LibCategory libCategory);
    public int updateLibCategory(LibCategory libCategory);
    public int deleteLibCategoryByCategoryIds(Long[] categoryIds);
    public int deleteLibCategoryByCategoryId(Long categoryId);
}
