package top.zhangqianxi.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import top.zhangqianxi.annotation.OptLog;
import top.zhangqianxi.dto.CommentBackDTO;
import top.zhangqianxi.dto.CommentDTO;
import top.zhangqianxi.dto.ReplyDTO;
import top.zhangqianxi.service.CommentService;
import top.zhangqianxi.util.Result;
import top.zhangqianxi.vo.CommentVO;
import top.zhangqianxi.vo.ConditionVO;
import top.zhangqianxi.vo.PageResult;
import top.zhangqianxi.vo.ReviewVO;

import javax.validation.Valid;
import java.util.List;

import static top.zhangqianxi.constant.OptTypeConst.REMOVE;
import static top.zhangqianxi.constant.OptTypeConst.UPDATE;

/**
 * ClassName: CommentController
 * Description: 评论模块
 *
 */
@Api(tags = "评论模块")
@RestController
public class CommentController {

    @Autowired
    private CommentService commentService;

    /**
     * 查询评论
     *
     * @param commentVO 评论信息
     * @return {@link Result<CommentDTO>}
     */
    @ApiOperation(value = "查询评论")
    @GetMapping("/comments")
    public Result<PageResult<CommentDTO>> listComments(CommentVO commentVO) {
        return Result.ok(commentService.listComments(commentVO));
    }

    /**
     * 添加评论
     *
     * @param commentVO 评论信息
     * @return {@link Result<>}
     */
    @ApiOperation(value = "添加评论")
    @PostMapping("/comments")
    public Result<?> saveComment(@Valid @RequestBody CommentVO commentVO) {
        commentService.saveComment(commentVO);
        return Result.ok();
    }

    /**
     * 查询评论下的回复
     *
     * @param commentId 评论id
     * @return {@link Result<ReplyDTO>} 回复列表
     */
    @ApiOperation(value = "查询评论下的回复")
    @ApiImplicitParam(name = "commentId", value = "评论id", required = true, dataType = "Integer")
    @GetMapping("/comments/{commentId}/replies")
    public Result<List<ReplyDTO>> listRepliesByCommentId(@PathVariable("commentId") Integer commentId) {
        return Result.ok(commentService.listRepliesByCommentId(commentId));
    }

    /**
     * 评论点赞
     *
     * @param commentId 评论id
     * @return {@link Result<>}
     */
    @ApiOperation(value = "评论点赞")
    @PostMapping("/comments/{commentId}/like")
    public Result<?> saveCommentLike(@PathVariable("commentId") Integer commentId) {
        commentService.saveCommentLike(commentId);
        return Result.ok();
    }

    /**
     * 审核评论
     *
     * @param reviewVO 审核信息
     * @return {@link Result<>}
     */
    @OptLog(optType = UPDATE)
    @ApiOperation(value = "审核评论")
    @PutMapping("/admin/comments/review")
    public Result<?> updateCommentsReview(@Valid @RequestBody ReviewVO reviewVO) {
        commentService.updateCommentsReview(reviewVO);
        return Result.ok();
    }

    /**
     * 删除评论
     *
     * @param commentIdList 评论id列表
     * @return {@link Result<>}
     */
    @OptLog(optType = REMOVE)
    @ApiOperation(value = "删除评论")
    @DeleteMapping("/admin/comments")
    public Result<?> deleteComments(@RequestBody List<Integer> commentIdList) {
        commentService.removeByIds(commentIdList);
        return Result.ok();
    }

    /**
     * 查询后台评论
     *
     * @param condition 条件
     * @return {@link Result<CommentBackDTO>} 后台评论
     */
    @ApiOperation(value = "查询后台评论")
    @GetMapping("/admin/comments")
    public Result<PageResult<CommentBackDTO>> listCommentBackDTO(ConditionVO condition) {
        return Result.ok(commentService.listCommentBackDTO(condition));
    }
}
