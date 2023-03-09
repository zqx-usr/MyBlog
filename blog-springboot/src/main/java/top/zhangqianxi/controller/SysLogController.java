package top.zhangqianxi.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.zhangqianxi.entity.SysLog;
import top.zhangqianxi.exception.CustomException;
import top.zhangqianxi.service.SysLogService;
import top.zhangqianxi.util.Result;
import top.zhangqianxi.vo.ListSysLogVO;

/**
 * ClassName: SysLogController
 * Description: 系统异常日志模块
 *
 */
@Log4j2
@Api(tags = "系统异常日志模块")
@RestController
@RequestMapping(value = "/sys/log")
public class SysLogController {

    @Autowired
    private SysLogService sysLogService;

    /**
     * 系统异常日志列表查询
     * @param listSysLogDTO
     * @return
     */
    @GetMapping(value = "page")
    @ApiOperation(value = "查看系统异常日志")
    public Result<Page<SysLog>> page(ListSysLogVO listSysLogDTO) {
        log.info("系统异常日志列表查询");
        QueryWrapper<SysLog> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("create_date");
        Page<SysLog> page = sysLogService.page(new Page<>(listSysLogDTO.getCurrentPage(), listSysLogDTO.getPageSize()), queryWrapper);
        return Result.ok(page);
    }

    @ApiOperation(value = "测试接口")
    @GetMapping("/test")
    public Result<String> test(){
        throw new CustomException("日志接口出现异常出现");
    }

}
