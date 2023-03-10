package top.zhangqianxi.mapper;

import org.apache.ibatis.annotations.Mapper;
import top.zhangqianxi.entity.Photo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
* @author 86152
* @description 针对表【tb_photo(照片)】的数据库操作Mapper
* @createDate 2023-01-05 11:03:38
* @Entity top.zhangqianxi.entity.Photo
*/
@Mapper
public interface PhotoMapper extends BaseMapper<Photo> {

}




