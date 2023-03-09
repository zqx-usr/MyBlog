package top.zhangqianxi.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import top.zhangqianxi.dto.OperationLogDTO;
import top.zhangqianxi.entity.OperationLog;
import top.zhangqianxi.service.OperationLogService;
import top.zhangqianxi.mapper.OperationLogMapper;
import org.springframework.stereotype.Service;
import top.zhangqianxi.util.BeanCopyUtils;
import top.zhangqianxi.util.PageUtils;
import top.zhangqianxi.vo.ConditionVO;
import top.zhangqianxi.vo.PageResult;

import java.util.List;

/**
* @author 86152
* @description 针对表【tb_operation_log】的数据库操作Service实现
* @createDate 2023-01-05 11:03:22
*/
@Service
public class OperationLogServiceImpl extends ServiceImpl<OperationLogMapper, OperationLog> implements OperationLogService{
    @Override
    public PageResult<OperationLogDTO> listOperationLogs(ConditionVO conditionVO) {
        Page<OperationLog> page = new Page<>(PageUtils.getCurrent(), PageUtils.getSize());
        // 查询日志列表
        Page<OperationLog> operationLogPage = this.page(page, new LambdaQueryWrapper<OperationLog>()
                .like(StringUtils.isNotBlank(conditionVO.getKeywords()), OperationLog::getOptModule, conditionVO.getKeywords())
                .or()
                .like(StringUtils.isNotBlank(conditionVO.getKeywords()), OperationLog::getOptDesc, conditionVO.getKeywords())
                .orderByDesc(OperationLog::getId));
        List<OperationLogDTO> operationLogDTOList = BeanCopyUtils.copyList(operationLogPage.getRecords(), OperationLogDTO.class);
        return new PageResult<>(operationLogDTOList, (int) operationLogPage.getTotal());
    }

}




