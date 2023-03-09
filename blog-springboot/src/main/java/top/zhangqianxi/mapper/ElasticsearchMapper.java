package top.zhangqianxi.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import top.zhangqianxi.dto.ArticleSearchDTO;

/**
 * ClassName: ElasticsearchMapper
 * Description: elasticsearch
 *
 */
@Mapper
public interface ElasticsearchMapper extends ElasticsearchRepository<ArticleSearchDTO,Integer> {
}
