package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.LibFine;

/**
 * 借阅罚金Service接口
 * 
 * @author ruoyi
 */
public interface ILibFineService 
{
    /** 查询罚金列表 */
    public List<LibFine> selectLibFineList(LibFine libFine);

    /** 确认缴纳罚金 */
    public void pay(Long fineId);

    /** 查询某读者的罚金列表 */
    public List<LibFine> selectMyFines(Long userId);
}
