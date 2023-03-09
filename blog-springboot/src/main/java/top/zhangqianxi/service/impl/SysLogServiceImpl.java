package top.zhangqianxi.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import top.zhangqianxi.entity.SysLog;
import top.zhangqianxi.service.SysLogService;
import top.zhangqianxi.mapper.SysLogMapper;

/**
 * ClassName: SysLogServiceImpl
 * Package: top.zhangqianxi.service.impl
 * Description: 系统日志Service接口实现类
 *
 * @Author: zhangqianxi
 * @Create: 2022/12/7 14:14
 * @Version: V1.0
 */
@Service
public class SysLogServiceImpl extends ServiceImpl<SysLogMapper, SysLog> implements SysLogService {
}
