package top.zhangqianxi.mapper;

import org.apache.ibatis.annotations.Mapper;
import top.zhangqianxi.entity.ArticleTag;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
* @author 86152
* @description 针对表【tb_article_tag】的数据库操作Mapper
* @createDate 2023-01-05 11:01:56
* @Entity top.zhangqianxi.entity.ArticleTag
*/
@Mapper
public interface ArticleTagMapper extends BaseMapper<ArticleTag> {

}




