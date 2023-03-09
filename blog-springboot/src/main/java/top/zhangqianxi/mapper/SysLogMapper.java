package top.zhangqianxi.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import top.zhangqianxi.entity.SysLog;

/**
 * ClassName: SysLogMapper
 * Package: top.zhangqianxi.dao
 * Description: 系统日志Mapper
 *
 */
@Mapper
public interface SysLogMapper extends BaseMapper<SysLog> {
}
