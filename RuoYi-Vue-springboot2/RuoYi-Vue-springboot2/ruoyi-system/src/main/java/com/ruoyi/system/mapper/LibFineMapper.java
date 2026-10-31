package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.LibFine;

/**
 * 借阅罚金Mapper接口
 * 
 * @author ruoyi
 */
public interface LibFineMapper 
{
    /** 查询罚金 */
    public LibFine selectLibFineByFineId(Long fineId);

    /** 查询罚金列表 */
    public List<LibFine> selectLibFineList(LibFine libFine);

    /** 新增罚金 */
    public int insertLibFine(LibFine libFine);

    /** 修改罚金 */
    public int updateLibFine(LibFine libFine);

    /** 删除罚金 */
    public int deleteLibFineByFineId(Long fineId);

    /** 批量删除罚金 */
    public int deleteLibFineByFineIds(Long[] fineIds);

    /** 根据借阅记录查询罚金（用于幂等生成） */
    public LibFine selectLibFineByBorrowId(@org.apache.ibatis.annotations.Param("borrowId") Long borrowId);

    /** 查询某读者的罚金列表（含书名、读者名） */
    public List<LibFine> selectMyFineList(@org.apache.ibatis.annotations.Param("userId") Long userId);

    /** 查询某读者未缴罚金数量（用于魔丸/灵珠判定） */
    public int countUnpaidByUserId(@org.apache.ibatis.annotations.Param("userId") Long userId);

    /** 统计某读者累计罚金笔数（含已缴，用于称号判定） */
    public int countFineByUserId(@org.apache.ibatis.annotations.Param("userId") Long userId);
}
