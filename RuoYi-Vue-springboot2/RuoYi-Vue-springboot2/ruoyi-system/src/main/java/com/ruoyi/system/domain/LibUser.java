package com.ruoyi.system.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 图书读者对象 lib_user
 * 
 * @author ruoyi
 * @date 2026-10-19
 */
public class LibUser extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 读者ID */
    private Long userId;

    /** 读者姓名 */
    @Excel(name = "读者姓名")
    private String userName;

    /** 联系电话 */
    @Excel(name = "联系电话")
    private String userPhone;

    /** 邮箱 */
    @Excel(name = "邮箱")
    private String userEmail;

    /** 状态 0正常 1禁用 */
    @Excel(name = "状态 0正常 1禁用")
    private String userStatus;

    /** 关联系统用户ID(注册读者绑定sys_user) */
    private Long sysUserId;

    /** 功德值（决定境界） */
    private Integer meritPoints;

    /** 魔性值（决定魔丸/灵珠） */
    private Integer demonValue;

    /** 境界等级 0凡人1道童2天兵3仙将4三坛海会大神5魔童战神 */
    private Integer cultivationLevel;

    /** 最后签到日期 */
    private java.util.Date lastSignDate;

    /** 最后奇遇日期 */
    private java.util.Date lastFortuneDate;

    /** 当前逾期未还数量（非持久化，仅查询时统计填充） */
    private Integer overdueCount;

    /** 未缴罚金数量（非持久化，仅查询时统计填充） */
    private Integer unpaidFineCount;

    /** 免罚令数量 */
    private Integer forgiveTokens;

    /** 最近开仙丹盲盒日期 */
    private java.util.Date boxDate;

    /** 当日开盲盒次数 */
    private Integer boxCountToday;

    public void setUserId(Long userId) 
    {
        this.userId = userId;
    }

    public Long getUserId() 
    {
        return userId;
    }

    public void setUserName(String userName) 
    {
        this.userName = userName;
    }

    public String getUserName() 
    {
        return userName;
    }

    public void setUserPhone(String userPhone) 
    {
        this.userPhone = userPhone;
    }

    public String getUserPhone() 
    {
        return userPhone;
    }

    public void setUserEmail(String userEmail) 
    {
        this.userEmail = userEmail;
    }

    public String getUserEmail() 
    {
        return userEmail;
    }

    public void setUserStatus(String userStatus) 
    {
        this.userStatus = userStatus;
    }

    public String getUserStatus() 
    {
        return userStatus;
    }

    public void setSysUserId(Long sysUserId) 
    {
        this.sysUserId = sysUserId;
    }

    public Long getSysUserId() 
    {
        return sysUserId;
    }

    public void setMeritPoints(Integer meritPoints)
    {
        this.meritPoints = meritPoints;
    }

    public Integer getMeritPoints()
    {
        return meritPoints;
    }

    public void setDemonValue(Integer demonValue)
    {
        this.demonValue = demonValue;
    }

    public Integer getDemonValue()
    {
        return demonValue;
    }

    public void setCultivationLevel(Integer cultivationLevel)
    {
        this.cultivationLevel = cultivationLevel;
    }

    public Integer getCultivationLevel()
    {
        return cultivationLevel;
    }

    public void setLastSignDate(java.util.Date lastSignDate)
    {
        this.lastSignDate = lastSignDate;
    }

    public java.util.Date getLastSignDate()
    {
        return lastSignDate;
    }

    public void setLastFortuneDate(java.util.Date lastFortuneDate)
    {
        this.lastFortuneDate = lastFortuneDate;
    }

    public java.util.Date getLastFortuneDate()
    {
        return lastFortuneDate;
    }

    public void setOverdueCount(Integer overdueCount)
    {
        this.overdueCount = overdueCount;
    }

    public Integer getOverdueCount()
    {
        return overdueCount;
    }

    public void setUnpaidFineCount(Integer unpaidFineCount)
    {
        this.unpaidFineCount = unpaidFineCount;
    }

    public Integer getUnpaidFineCount()
    {
        return unpaidFineCount;
    }

    public void setForgiveTokens(Integer forgiveTokens)
    {
        this.forgiveTokens = forgiveTokens;
    }

    public Integer getForgiveTokens()
    {
        return forgiveTokens;
    }

    public void setBoxDate(java.util.Date boxDate)
    {
        this.boxDate = boxDate;
    }

    public java.util.Date getBoxDate()
    {
        return boxDate;
    }

    public void setBoxCountToday(Integer boxCountToday)
    {
        this.boxCountToday = boxCountToday;
    }

    public Integer getBoxCountToday()
    {
        return boxCountToday;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("userId", getUserId())
            .append("userName", getUserName())
            .append("userPhone", getUserPhone())
            .append("userEmail", getUserEmail())
            .append("userStatus", getUserStatus())
            .append("sysUserId", getSysUserId())
            .append("meritPoints", getMeritPoints())
            .append("demonValue", getDemonValue())
            .append("cultivationLevel", getCultivationLevel())
            .append("lastSignDate", getLastSignDate())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
