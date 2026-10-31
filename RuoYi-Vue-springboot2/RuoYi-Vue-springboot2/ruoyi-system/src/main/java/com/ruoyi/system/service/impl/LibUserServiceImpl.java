package com.ruoyi.system.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.system.mapper.LibBorrowMapper;
import com.ruoyi.system.mapper.LibUserMapper;
import com.ruoyi.system.mapper.SysUserMapper;
import com.ruoyi.system.domain.LibUser;
import com.ruoyi.system.service.ILibUserService;
import com.ruoyi.system.service.ISysUserService;

/**
 * 图书读者Service业务层处理
 * 
 * @author ruoyi
 * @date 2026-10-19
 */
@Service
public class LibUserServiceImpl implements ILibUserService 
{
    @Autowired
    private LibUserMapper libUserMapper;

    @Autowired
    private LibBorrowMapper libBorrowMapper;

    @Autowired
    private ISysUserService sysUserService;

    @Autowired
    private SysUserMapper sysUserMapper;

    /**
     * 查询图书读者
     * 
     * @param userId 图书读者主键
     * @return 图书读者
     */
    @Override
    public LibUser selectLibUserByUserId(Long userId)
    {
        return libUserMapper.selectLibUserByUserId(userId);
    }

    /**
     * 查询图书读者列表
     * 
     * @param libUser 图书读者
     * @return 图书读者
     */
    @Override
    public List<LibUser> selectLibUserList(LibUser libUser)
    {
        return libUserMapper.selectLibUserList(libUser);
    }

    /**
     * 新增图书读者
     * 
     * @param libUser 图书读者
     * @return 结果
     */
    @Override
    public int insertLibUser(LibUser libUser)
    {
        libUser.setCreateTime(DateUtils.getNowDate());
        return libUserMapper.insertLibUser(libUser);
    }

    /**
     * 修改图书读者：同步更新其登录账号（姓名、电话、邮箱、启用/停用状态）
     *
     * @param libUser 图书读者
     * @return 结果
     */
    @Override
    public int updateLibUser(LibUser libUser)
    {
        libUser.setUpdateTime(DateUtils.getNowDate());
        int rows = libUserMapper.updateLibUser(libUser);
        // 同步系统登录账号，保证管理员的调整对登录生效
        LibUser old = libUserMapper.selectLibUserByUserId(libUser.getUserId());
        if (old != null && old.getSysUserId() != null)
        {
            com.ruoyi.common.core.domain.entity.SysUser sysUser = sysUserService.selectUserById(old.getSysUserId());
            if (sysUser != null)
            {
                sysUser.setNickName(libUser.getUserName());
                sysUser.setPhonenumber(libUser.getUserPhone());
                sysUser.setEmail(libUser.getUserEmail());
                sysUser.setStatus(libUser.getUserStatus());
                // 直接走 Mapper，仅更新 sys_user 字段，避免 service 层清空读者角色绑定
                sysUserMapper.updateUser(sysUser);
            }
        }
        return rows;
    }

    /**
     * 批量删除图书读者：先校验是否有未归还借阅，再联动删除其登录账号（sys_user + 角色绑定）
     *
     * @param userIds 需要删除的图书读者主键
     * @return 结果
     */
    @Override
    public int deleteLibUserByUserIds(Long[] userIds)
    {
        int count = 0;
        for (Long userId : userIds)
        {
            LibUser libUser = libUserMapper.selectLibUserByUserId(userId);
            if (libUser == null)
            {
                continue;
            }
            int active = libBorrowMapper.countActiveByUserId(userId);
            if (active > 0)
            {
                throw new ServiceException("读者「" + libUser.getUserName() + "」还有 " + active
                        + " 本图书未归还，无法删除，请先处理其借阅记录");
            }
            // 删除关联的系统登录账号（同时清理用户角色绑定）
            if (libUser.getSysUserId() != null)
            {
                sysUserService.deleteUserById(libUser.getSysUserId());
            }
            count += libUserMapper.deleteLibUserByUserId(userId);
        }
        return count;
    }

    /**
     * 删除图书读者信息（联动删除登录账号）
     *
     * @param userId 图书读者主键
     * @return 结果
     */
    @Override
    public int deleteLibUserByUserId(Long userId)
    {
        return deleteLibUserByUserIds(new Long[] { userId });
    }
}
