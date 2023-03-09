package top.zhangqianxi.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import top.zhangqianxi.dto.*;
import top.zhangqianxi.entity.Article;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import top.zhangqianxi.vo.ConditionVO;

import java.util.List;

/**
* @author 86152
* @description 针对表【tb_article】的数据库操作Mapper
* @createDate 2023-01-05 11:01:49
* @Entity top.zhangqianxi.entity.Article
*/
@Mapper
public interface ArticleMapper extends BaseMapper<Article> {

    /**
     * 文章统计
     *
     * @return {@link List<ArticleStatisticsDTO>} 文章统计结果
     */
    List<ArticleStatisticsDTO> listArticleStatistics();

    /**
     * 查询后台文章总量
     *
     * @param condition 条件
     * @return 文章总量
     */
    Integer countArticleBacks(@Param("condition")  ConditionVO condition);


    /**
     * 查询后台文章
     *
     * @param current   页码
     * @param size      大小
     * @param condition 条件
     * @return 文章列表
     */
    List<ArticleBackDTO> listArticleBacks(@Param("current") Long current, @Param("size") Long size, @Param("condition") ConditionVO condition);

    /**
     * 查询首页文章
     *
     * @param current 页码
     * @param size    大小
     * @return 文章列表
     */
    List<ArticleHomeDTO> listArticles(@Param("current") Long current, @Param("size") Long size);

    /**
     * 查看文章的推荐文章
     *
     * @param articleId 文章id
     * @return 文章列表
     */
    List<ArticleRecommendDTO> listRecommendArticles(@Param("articleId") Integer articleId);

    /**
     * 根据id查询文章
     *
     * @param articleId 文章id
     * @return 文章信息
     */
    ArticleDTO getArticleById(@Param("articleId") Integer articleId);

    /**
     * 根据条件查询文章
     *
     * @param current   页码
     * @param size      大小
     * @param condition 条件
     * @return 文章列表
     */
    List<ArticlePreviewDTO> listArticlesByCondition(@Param("current") Long current, @Param("size") Long size, @Param("condition") ConditionVO condition);

}




