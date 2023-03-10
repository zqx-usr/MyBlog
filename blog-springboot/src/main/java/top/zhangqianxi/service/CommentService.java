package top.zhangqianxi.service;

import top.zhangqianxi.dto.CommentBackDTO;
import top.zhangqianxi.dto.CommentDTO;
import top.zhangqianxi.dto.ReplyDTO;
import top.zhangqianxi.entity.Comment;
import com.baomidou.mybatisplus.extension.service.IService;
import top.zhangqianxi.vo.CommentVO;
import top.zhangqianxi.vo.ConditionVO;
import top.zhangqianxi.vo.PageResult;
import top.zhangqianxi.vo.ReviewVO;

import java.util.List;

/**
* @author 86152
* @description 针对表【tb_comment】的数据库操作Service
* @createDate 2023-01-05 11:02:13
*/
public interface CommentService extends IService<Comment> {
    /**
     * 查看评论
     *
     * @param commentVO 评论信息
     * @return 评论列表
     */
    PageResult<CommentDTO> listComments(CommentVO commentVO);

    /**
     * 查看评论下的回复
     *
     * @param commentId 评论id
     * @return 回复列表
     */
    List<ReplyDTO> listRepliesByCommentId(Integer commentId);

    /**
     * 添加评论
     *
     * @param commentVO 评论对象
     */
    void saveComment(CommentVO commentVO);

    /**
     * 点赞评论
     *
     * @param commentId 评论id
     */
    void saveCommentLike(Integer commentId);

    /**
     * 审核评论
     *
     * @param reviewVO 审核信息
     */
    void updateCommentsReview(ReviewVO reviewVO);

    /**
     * 查询后台评论
     *
     * @param condition 条件
     * @return 评论列表
     */
    PageResult<CommentBackDTO> listCommentBackDTO(ConditionVO condition);

}
