package top.zhangqianxi.service;

import top.zhangqianxi.dto.CategoryBackDTO;
import top.zhangqianxi.dto.CategoryDTO;
import top.zhangqianxi.dto.CategoryOptionDTO;
import top.zhangqianxi.entity.Category;
import com.baomidou.mybatisplus.extension.service.IService;
import top.zhangqianxi.vo.CategoryVO;
import top.zhangqianxi.vo.ConditionVO;
import top.zhangqianxi.vo.PageResult;

import java.util.List;

/**
* @author 86152
* @description 针对表【tb_category】的数据库操作Service
* @createDate 2023-01-05 11:02:01
*/
public interface CategoryService extends IService<Category> {

    /**
     * 查询分类列表
     *
     * @return 分类列表
     */
    PageResult<CategoryDTO> listCategories();

    /**
     * 查询后台分类
     *
     * @param conditionVO 条件
     * @return {@link PageResult<CategoryBackDTO>} 后台分类
     */
    PageResult<CategoryBackDTO> listBackCategories(ConditionVO conditionVO);

    /**
     * 搜索文章分类
     *
     * @param condition 条件
     * @return {@link List<CategoryOptionDTO>} 分类列表
     */
    List<CategoryOptionDTO> listCategoriesBySearch(ConditionVO condition);

    /**
     * 删除分类
     *
     * @param categoryIdList 分类id集合
     */
    void deleteCategory(List<Integer> categoryIdList);

    /**
     * 添加或修改分类
     *
     * @param categoryVO 分类
     */
    void saveOrUpdateCategory(CategoryVO categoryVO);

}
