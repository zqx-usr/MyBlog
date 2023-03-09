package top.zhangqianxi.service;

import com.baomidou.mybatisplus.extension.service.IService;
import top.zhangqianxi.dto.LabelOptionDTO;
import top.zhangqianxi.dto.MenuDTO;
import top.zhangqianxi.dto.UserMenuDTO;
import top.zhangqianxi.entity.Menu;
import top.zhangqianxi.vo.ConditionVO;
import top.zhangqianxi.vo.MenuVO;

import java.util.List;

/**
* @author 86152
* @description 针对表【tb_menu】的数据库操作Service
* @createDate 2023-01-04 15:21:53
*/
public interface MenuService extends IService<Menu> {
    /**
     * 查看用户菜单
     *
     * @return 菜单列表
     */
    List<UserMenuDTO> listUserMenus();

    /**
     * 查看菜单列表
     *
     * @param conditionVO 条件
     * @return 菜单列表
     */
    List<MenuDTO> listMenus(ConditionVO conditionVO);

    /**
     * 新增或修改菜单
     *
     * @param menuVO 菜单信息
     */
    void saveOrUpdateMenu(MenuVO menuVO);

    /**
     * 删除菜单
     *
     * @param menuId 菜单id
     */
    void deleteMenu(Integer menuId);

    /**
     * 查看角色菜单选项
     *
     * @return 角色菜单选项
     */
    List<LabelOptionDTO> listMenuOptions();

}
