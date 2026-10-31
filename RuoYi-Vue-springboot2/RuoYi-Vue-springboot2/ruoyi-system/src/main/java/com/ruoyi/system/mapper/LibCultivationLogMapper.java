package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.LibCultivationLog;

/**
 * 读者修行流水Mapper接口
 *
 * @author ruoyi
 */
public interface LibCultivationLogMapper
{
    /** 新增流水 */
    public int insertLibCultivationLog(LibCultivationLog log);

    /** 查询某读者的修行流水 */
    public List<LibCultivationLog> selectLogsByUserId(Long userId);
}
