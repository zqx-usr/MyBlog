package top.zhangqianxi.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import top.zhangqianxi.annotation.OptLog;
import top.zhangqianxi.dto.RoleDTO;
import top.zhangqianxi.dto.UserRoleDTO;
import top.zhangqianxi.service.RoleService;
import top.zhangqianxi.util.Result;
import top.zhangqianxi.vo.ConditionVO;
import top.zhangqianxi.vo.PageResult;
import top.zhangqianxi.vo.RoleVO;

import javax.validation.Valid;
import java.util.List;

import static top.zhangqianxi.constant.OptTypeConst.REMOVE;
import static top.zhangqianxi.constant.OptTypeConst.SAVE_OR_UPDATE;

/**
 * ClassName: RoleController
 * Description: 角色模块
 *
 */
@Api(tags = "角色模块")
@RestController
public class RoleController {
    @Autowired
    private RoleService roleService;

    /**
     * 查询用户角色选项
     *
     * @return {@link Result<UserRoleDTO>} 用户角色选项
     */
    @ApiOperation(value = "查询用户角色选项")
    @GetMapping("/admin/users/role")
    public Result<List<UserRoleDTO>> listUserRoles() {
        return Result.ok(roleService.listUserRoles());
    }

    /**
     * 查询角色列表
     *
     * @param conditionVO 条件
     * @return {@link Result<RoleDTO>} 角色列表
     */
    @ApiOperation(value = "查询角色列表")
    @GetMapping("/admin/roles")
    public Result<PageResult<RoleDTO>> listRoles(ConditionVO conditionVO) {
        return Result.ok(roleService.listRoles(conditionVO));
    }

    /**
     * 保存或更新角色
     *
     * @param roleVO 角色信息
     * @return {@link Result<>}
     */
    @OptLog(optType = SAVE_OR_UPDATE)
    @ApiOperation(value = "保存或更新角色")
    @PostMapping("/admin/role")
    public Result<?> saveOrUpdateRole(@RequestBody @Valid RoleVO roleVO) {
        roleService.saveOrUpdateRole(roleVO);
        return Result.ok();
    }

    /**
     * 删除角色
     *
     * @param roleIdList 角色id列表
     * @return {@link Result<>}
     */
    @OptLog(optType = REMOVE)
    @ApiOperation(value = "删除角色")
    @DeleteMapping("/admin/roles")
    public Result<?> deleteRoles(@RequestBody List<Integer> roleIdList) {
        roleService.deleteRoles(roleIdList);
        return Result.ok();
    }

}
