package top.zhangqianxi.service;

import top.zhangqianxi.dto.TagBackDTO;
import top.zhangqianxi.dto.TagDTO;
import top.zhangqianxi.entity.Tag;
import com.baomidou.mybatisplus.extension.service.IService;
import top.zhangqianxi.vo.ConditionVO;
import top.zhangqianxi.vo.PageResult;
import top.zhangqianxi.vo.TagVO;

import java.util.List;

/**
* @author 86152
* @description 针对表【tb_tag】的数据库操作Service
* @createDate 2023-01-05 11:04:24
*/
public interface TagService extends IService<Tag> {
    /**
     * 查询标签列表
     *
     * @return 标签列表
     */
    PageResult<TagDTO> listTags();

    /**
     * 查询后台标签
     *
     * @param condition 条件
     * @return {@link PageResult<TagBackDTO>} 标签列表
     */
    PageResult<TagBackDTO> listTagBackDTO(ConditionVO condition);

    /**
     * 搜索文章标签
     *
     * @param condition 条件
     * @return {@link List <TagDTO>} 标签列表
     */
    List<TagDTO> listTagsBySearch(ConditionVO condition);

    /**
     * 删除标签
     *
     * @param tagIdList 标签id集合
     */
    void deleteTag(List<Integer> tagIdList);

    /**
     * 保存或更新标签
     *
     * @param tagVO 标签
     */
    void saveOrUpdateTag(TagVO tagVO);

}
