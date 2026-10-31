package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.LibUser;

/**
 * 图书读者Service接口
 * 
 * @author ruoyi
 * @date 2026-10-19
 */
public interface ILibUserService 
{
    /**
     * 查询图书读者
     * 
     * @param userId 图书读者主键
     * @return 图书读者
     */
    public LibUser selectLibUserByUserId(Long userId);

    /**
     * 查询图书读者列表
     * 
     * @param libUser 图书读者
     * @return 图书读者集合
     */
    public List<LibUser> selectLibUserList(LibUser libUser);

    /**
     * 新增图书读者
     * 
     * @param libUser 图书读者
     * @return 结果
     */
    public int insertLibUser(LibUser libUser);

    /**
     * 修改图书读者
     * 
     * @param libUser 图书读者
     * @return 结果
     */
    public int updateLibUser(LibUser libUser);

    /**
     * 批量删除图书读者
     * 
     * @param userIds 需要删除的图书读者主键集合
     * @return 结果
     */
    public int deleteLibUserByUserIds(Long[] userIds);

    /**
     * 删除图书读者信息
     * 
     * @param userId 图书读者主键
     * @return 结果
     */
    public int deleteLibUserByUserId(Long userId);
}
