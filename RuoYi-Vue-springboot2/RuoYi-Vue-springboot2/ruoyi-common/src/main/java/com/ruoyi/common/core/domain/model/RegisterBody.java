package com.ruoyi.common.core.domain.model;

/**
 * 用户注册对象
 * 
 * @author ruoyi
 */
public class RegisterBody extends LoginBody
{
    /** 读者姓名（图书馆注册扩展） */
    private String realName;

    /** 联系电话（图书馆注册扩展） */
    private String phone;

    /** 邮箱（图书馆注册扩展） */
    private String email;

    public void setRealName(String realName) { this.realName = realName; }
    public String getRealName() { return realName; }

    public void setPhone(String phone) { this.phone = phone; }
    public String getPhone() { return phone; }

    public void setEmail(String email) { this.email = email; }
    public String getEmail() { return email; }
}
