package top.zhangqianxi.service;

import top.zhangqianxi.dto.OperationLogDTO;
import top.zhangqianxi.entity.OperationLog;
import com.baomidou.mybatisplus.extension.service.IService;
import top.zhangqianxi.vo.ConditionVO;
import top.zhangqianxi.vo.PageResult;

/**
* @author 86152
* @description 针对表【tb_operation_log】的数据库操作Service
* @createDate 2023-01-05 11:03:22
*/
public interface OperationLogService extends IService<OperationLog> {
    /**
     * 查询日志列表
     *
     * @param conditionVO 条件
     * @return 日志列表
     */
    PageResult<OperationLogDTO> listOperationLogs(ConditionVO conditionVO);
}
