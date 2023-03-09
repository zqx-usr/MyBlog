package top.zhangqianxi.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import top.zhangqianxi.constant.CommonConst;
import top.zhangqianxi.dto.RoleDTO;
import top.zhangqianxi.dto.UserRoleDTO;
import top.zhangqianxi.entity.Role;
import top.zhangqianxi.entity.RoleMenu;
import top.zhangqianxi.entity.RoleResource;
import top.zhangqianxi.entity.UserRole;
import top.zhangqianxi.exception.BizException;
import top.zhangqianxi.handler.FilterInvocationSecurityMetadataSourceImpl;
import top.zhangqianxi.mapper.UserRoleMapper;
import top.zhangqianxi.service.RoleMenuService;
import top.zhangqianxi.service.RoleResourceService;
import top.zhangqianxi.service.RoleService;
import top.zhangqianxi.mapper.RoleMapper;
import org.springframework.stereotype.Service;
import top.zhangqianxi.util.BeanCopyUtils;
import top.zhangqianxi.util.PageUtils;
import top.zhangqianxi.vo.ConditionVO;
import top.zhangqianxi.vo.PageResult;
import top.zhangqianxi.vo.RoleVO;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
* @author 86152
* @description 针对表【tb_role】的数据库操作Service实现
* @createDate 2023-01-06 16:11:19
*/
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService{
    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private RoleResourceService roleResourceService;
    @Autowired
    private RoleMenuService roleMenuService;
    @Autowired
    private UserRoleMapper userRoleMapper;
    @Autowired
    private FilterInvocationSecurityMetadataSourceImpl filterInvocationSecurityMetadataSource;

    @Override
    public List<UserRoleDTO> listUserRoles() {
        // 查询角色列表
        List<Role> roleList = roleMapper.selectList(new LambdaQueryWrapper<Role>()
                .select(Role::getId, Role::getRoleName));
        return BeanCopyUtils.copyList(roleList, UserRoleDTO.class);
    }

    @Override
    public PageResult<RoleDTO> listRoles(ConditionVO conditionVO) {
        // 查询角色列表
        List<RoleDTO> roleDTOList = roleMapper.listRoles(PageUtils.getLimitCurrent(), PageUtils.getSize(), conditionVO);
        // 查询总量
        Integer count = Math.toIntExact(roleMapper.selectCount(new LambdaQueryWrapper<Role>()
                .like(StringUtils.isNotBlank(conditionVO.getKeywords()), Role::getRoleName, conditionVO.getKeywords())));
        return new PageResult<>(roleDTOList, count);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void saveOrUpdateRole(RoleVO roleVO) {
        // 判断角色名重复
        Role existRole = roleMapper.selectOne(new LambdaQueryWrapper<Role>()
                .select(Role::getId)
                .eq(Role::getRoleName, roleVO.getRoleName()));
        if (Objects.nonNull(existRole) && !existRole.getId().equals(roleVO.getId())) {
            throw new BizException("角色名已存在");
        }
        // 保存或更新角色信息
        Role role = Role.builder()
                .id(roleVO.getId())
                .roleName(roleVO.getRoleName())
                .roleLabel(roleVO.getRoleLabel())
                .isDisable(CommonConst.FALSE)
                .build();
        this.saveOrUpdate(role);
        // 更新角色资源关系
        if (Objects.nonNull(roleVO.getResourceIdList())) {
            if (Objects.nonNull(roleVO.getId())) {
                roleResourceService.remove(new LambdaQueryWrapper<RoleResource>()
                        .eq(RoleResource::getRoleId, roleVO.getId()));
            }
            List<RoleResource> roleResourceList = roleVO.getResourceIdList().stream()
                    .map(resourceId -> RoleResource.builder()
                            .roleId(role.getId())
                            .resourceId(resourceId)
                            .build())
                    .collect(Collectors.toList());
            roleResourceService.saveBatch(roleResourceList);
            // 重新加载角色资源信息
            filterInvocationSecurityMetadataSource.clearDataSource();
        }
        // 更新角色菜单关系
        if (Objects.nonNull(roleVO.getMenuIdList())) {
            if (Objects.nonNull(roleVO.getId())) {
                roleMenuService.remove(new LambdaQueryWrapper<RoleMenu>().eq(RoleMenu::getRoleId, roleVO.getId()));
            }
            List<RoleMenu> roleMenuList = roleVO.getMenuIdList().stream()
                    .map(menuId -> RoleMenu.builder()
                            .roleId(role.getId())
                            .menuId(menuId)
                            .build())
                    .collect(Collectors.toList());
            roleMenuService.saveBatch(roleMenuList);
        }
    }

    @Override
    public void deleteRoles(List<Integer> roleIdList) {
        // 判断角色下是否有用户
        Integer count = Math.toIntExact(userRoleMapper.selectCount(new LambdaQueryWrapper<UserRole>()
                .in(UserRole::getRoleId, roleIdList)));
        if (count > 0) {
            throw new BizException("该角色下存在用户");
        }
        roleMapper.deleteBatchIds(roleIdList);
    }
}




