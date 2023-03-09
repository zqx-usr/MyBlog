package top.zhangqianxi.mapper;

import org.apache.ibatis.annotations.Mapper;
import top.zhangqianxi.entity.FriendLink;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
* @author 86152
* @description 针对表【tb_friend_link】的数据库操作Mapper
* @createDate 2023-01-05 11:02:22
* @Entity top.zhangqianxi.entity.FriendLink
*/
@Mapper
public interface FriendLinkMapper extends BaseMapper<FriendLink> {

}




