package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.LibCategory;

/**
 * 图书分类Mapper接口
 *
 * @author ruoyi
 */
public interface LibCategoryMapper
{
    public LibCategory selectLibCategoryByCategoryId(Long categoryId);
    public List<LibCategory> selectLibCategoryList(LibCategory libCategory);
    public int insertLibCategory(LibCategory libCategory);
    public int updateLibCategory(LibCategory libCategory);
    public int deleteLibCategoryByCategoryId(Long categoryId);
    public int deleteLibCategoryByCategoryIds(Long[] categoryIds);
}
