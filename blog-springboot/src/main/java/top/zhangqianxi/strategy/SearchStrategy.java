package top.zhangqianxi.strategy;

import top.zhangqianxi.dto.ArticleSearchDTO;

import java.util.List;

/**
 * ClassName: SearchStrategy
 * Description: 搜索策略
 *
 * @Author: zhangqianxi
 * @Create: 2023/1/6 10:48
 * @Version: V1.0
 */
public interface SearchStrategy {
    /**
     * 搜索文章
     *
     * @param keywords 关键字
     * @return {@link List <ArticleSearchDTO>} 文章列表
     */
    List<ArticleSearchDTO> searchArticle(String keywords);
}
