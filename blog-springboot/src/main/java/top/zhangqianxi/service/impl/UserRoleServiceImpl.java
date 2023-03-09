package top.zhangqianxi.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import top.zhangqianxi.entity.UserRole;
import top.zhangqianxi.service.UserRoleService;
import top.zhangqianxi.mapper.UserRoleMapper;
import org.springframework.stereotype.Service;

/**
* @author 86152
* @description 针对表【tb_user_role】的数据库操作Service实现
* @createDate 2023-01-05 11:04:49
*/
@Service
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole>
    implements UserRoleService{

}




