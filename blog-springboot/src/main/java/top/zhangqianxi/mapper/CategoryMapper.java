package top.zhangqianxi.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import top.zhangqianxi.dto.CategoryBackDTO;
import top.zhangqianxi.dto.CategoryDTO;
import top.zhangqianxi.entity.Category;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import top.zhangqianxi.vo.ConditionVO;

import java.util.List;

/**
 * @author 86152
 * @description 针对表【tb_category】的数据库操作Mapper
 * @createDate 2023-01-05 11:02:01
 * @Entity top.zhangqianxi.entity.Category
 */
@Mapper
public interface CategoryMapper extends BaseMapper<Category> {

    /**
     * 查询分类和对应文章数量
     *
     * @return 分类列表
     */
    List<CategoryDTO> listCategoryDTO();

    /**
     * 查询后台分类列表
     *
     * @param current   页码
     * @param size      大小
     * @param condition 条件
     * @return {@link List<CategoryBackDTO>} 分类列表
     */
    List<CategoryBackDTO> listCategoryBackDTO(@Param("current") Long current, @Param("size") Long size, @Param("condition") ConditionVO condition);

}




