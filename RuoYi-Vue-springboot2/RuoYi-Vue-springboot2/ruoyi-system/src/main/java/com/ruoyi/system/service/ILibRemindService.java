package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.LibRemind;

/**
 * 逾期催缴 Service 接口
 *
 * @author ruoyi
 */
public interface ILibRemindService
{
    /** 查询催缴列表 */
    public List<LibRemind> selectLibRemindList(LibRemind libRemind);

    /** 对某笔逾期借阅发起催缴，返回催缴记录 */
    public LibRemind urge(Long borrowId);

    /** 查询某读者的催缴记录（可按状态过滤） */
    public List<LibRemind> selectMyReminds(Long userId, String status);

    /** 标记某条催缴为已读 */
    public void markRead(Long remindId, Long userId);
}
