package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.LibRemind;
import org.apache.ibatis.annotations.Param;

/**
 * 逾期催缴 Mapper 接口
 *
 * @author ruoyi
 */
public interface LibRemindMapper
{
    /** 查询催缴记录 */
    public LibRemind selectLibRemindByRemindId(Long remindId);

    /** 查询催缴列表 */
    public List<LibRemind> selectLibRemindList(LibRemind libRemind);

    /** 查询某读者的催缴记录（可按状态过滤） */
    public List<LibRemind> selectMyRemindList(@Param("userId") Long userId, @Param("status") String status);

    /** 新增催缴 */
    public int insertLibRemind(LibRemind libRemind);

    /** 修改催缴 */
    public int updateLibRemind(LibRemind libRemind);

    /** 删除催缴 */
    public int deleteLibRemindByRemindId(Long remindId);
}
