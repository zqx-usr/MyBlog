package top.zhangqianxi.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import top.zhangqianxi.dto.FriendLinkBackDTO;
import top.zhangqianxi.dto.FriendLinkDTO;
import top.zhangqianxi.entity.FriendLink;
import top.zhangqianxi.service.FriendLinkService;
import top.zhangqianxi.mapper.FriendLinkMapper;
import org.springframework.stereotype.Service;
import top.zhangqianxi.util.BeanCopyUtils;
import top.zhangqianxi.util.PageUtils;
import top.zhangqianxi.vo.ConditionVO;
import top.zhangqianxi.vo.FriendLinkVO;
import top.zhangqianxi.vo.PageResult;

import java.util.List;

/**
* @author 86152
* @description 针对表【tb_friend_link】的数据库操作Service实现
* @createDate 2023-01-05 11:02:22
*/
@Service
public class FriendLinkServiceImpl extends ServiceImpl<FriendLinkMapper, FriendLink> implements FriendLinkService{
    @Autowired
    private FriendLinkMapper friendLinkMapper;

    @Override
    public List<FriendLinkDTO> listFriendLinks() {
        // 查询友链列表
        List<FriendLink> friendLinkList = friendLinkMapper.selectList(null);
        return BeanCopyUtils.copyList(friendLinkList, FriendLinkDTO.class);
    }

    @Override
    public PageResult<FriendLinkBackDTO> listFriendLinkDTO(ConditionVO condition) {
        // 分页查询友链列表
        Page<FriendLink> page = new Page<>(PageUtils.getCurrent(), PageUtils.getSize());
        Page<FriendLink> friendLinkPage = friendLinkMapper.selectPage(page, new LambdaQueryWrapper<FriendLink>()
                .like(StringUtils.isNotBlank(condition.getKeywords()), FriendLink::getLinkName, condition.getKeywords()));
        // 转换DTO
        List<FriendLinkBackDTO> friendLinkBackDTOList = BeanCopyUtils.copyList(friendLinkPage.getRecords(), FriendLinkBackDTO.class);
        return new PageResult<>(friendLinkBackDTOList, (int) friendLinkPage.getTotal());
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void saveOrUpdateFriendLink(FriendLinkVO friendLinkVO) {
        FriendLink friendLink = BeanCopyUtils.copyObject(friendLinkVO, FriendLink.class);
        this.saveOrUpdate(friendLink);
    }
}




