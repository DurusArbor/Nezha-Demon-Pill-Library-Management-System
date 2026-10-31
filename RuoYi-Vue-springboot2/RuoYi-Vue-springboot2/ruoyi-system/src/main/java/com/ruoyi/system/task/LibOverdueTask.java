package com.ruoyi.system.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.ruoyi.system.service.ILibBorrowService;

/**
 * 图书逾期定时任务（sys_job 中配置 invoke_target=libOverdueTask.scanOverdue()）
 * 每日扫描逾期借阅，生成罚金，处理预约过期
 * 
 * @author ruoyi
 */
@Component("libOverdueTask")
public class LibOverdueTask
{
    private static final Logger log = LoggerFactory.getLogger(LibOverdueTask.class);

    @Autowired
    private ILibBorrowService libBorrowService;

    public void scanOverdue()
    {
        try
        {
            libBorrowService.scanOverdue();
            log.info("逾期罚金扫描任务执行完成");
        }
        catch (Exception e)
        {
            log.error("逾期罚金扫描任务执行异常", e);
        }
    }
}
