package com.ruoyi.system.service.impl;

import java.util.Date;
import java.util.List;
import com.ruoyi.common.exception.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.LibFineMapper;
import com.ruoyi.system.domain.LibFine;
import com.ruoyi.system.service.ILibFineService;

/**
 * 借阅罚金Service业务层处理
 *
 * @author ruoyi
 */
@Service
public class LibFineServiceImpl implements ILibFineService
{
    @Autowired
    private LibFineMapper libFineMapper;

    @Override
    public List<LibFine> selectLibFineList(LibFine libFine) {
        return libFineMapper.selectLibFineList(libFine);
    }

    /**
     * 确认缴纳罚金
     */
    @Override
    public void pay(Long fineId) {
        LibFine fine = libFineMapper.selectLibFineByFineId(fineId);
        if (fine == null) {
            throw new ServiceException("罚金记录不存在");
        }
        if ("1".equals(fine.getFineStatus())) {
            throw new ServiceException("该罚金已缴纳");
        }
        LibFine update = new LibFine();
        update.setFineId(fineId);
        update.setFineStatus("1");
        update.setPayTime(new Date());
        update.setUpdateTime(new Date());
        libFineMapper.updateLibFine(update);
    }

    @Override
    public List<LibFine> selectMyFines(Long userId) {
        return libFineMapper.selectMyFineList(userId);
    }
}
