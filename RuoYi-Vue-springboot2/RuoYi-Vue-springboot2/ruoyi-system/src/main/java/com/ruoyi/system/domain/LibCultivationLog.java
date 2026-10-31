package com.ruoyi.system.domain;

import java.util.Date;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 读者修行流水对象 lib_cultivation_log
 *
 * @author ruoyi
 */
public class LibCultivationLog extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 流水ID */
    private Long logId;

    /** 读者ID */
    private Long userId;

    /** 类型 borrow/return_on_time/return_overdue/sign/purify */
    private String changeType;

    /** 功德变化 */
    private Integer meritChange;

    /** 魔性变化 */
    private Integer demonChange;

    /** 事由 */
    private String reason;

    public Long getLogId() { return logId; }
    public void setLogId(Long logId) { this.logId = logId; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getChangeType() { return changeType; }
    public void setChangeType(String changeType) { this.changeType = changeType; }

    public Integer getMeritChange() { return meritChange; }
    public void setMeritChange(Integer meritChange) { this.meritChange = meritChange; }

    public Integer getDemonChange() { return demonChange; }
    public void setDemonChange(Integer demonChange) { this.demonChange = demonChange; }

    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }

    @Override
    public Date getCreateTime() { return super.getCreateTime(); }
}
