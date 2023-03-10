package top.zhangqianxi.service.impl;

import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import top.zhangqianxi.dto.*;
import top.zhangqianxi.entity.Article;
import top.zhangqianxi.entity.ArticleTag;
import top.zhangqianxi.entity.Category;
import top.zhangqianxi.entity.Tag;
import top.zhangqianxi.enums.FileExtEnum;
import top.zhangqianxi.enums.FilePathEnum;
import top.zhangqianxi.exception.BizException;
import top.zhangqianxi.mapper.ArticleTagMapper;
import top.zhangqianxi.mapper.CategoryMapper;
import top.zhangqianxi.mapper.TagMapper;
import top.zhangqianxi.service.*;
import top.zhangqianxi.mapper.ArticleMapper;
import org.springframework.stereotype.Service;
import top.zhangqianxi.strategy.context.SearchStrategyContext;
import top.zhangqianxi.strategy.context.UploadStrategyContext;
import top.zhangqianxi.util.BeanCopyUtils;
import top.zhangqianxi.util.CommonUtils;
import top.zhangqianxi.util.PageUtils;
import top.zhangqianxi.util.UserUtils;
import top.zhangqianxi.vo.*;

import javax.servlet.http.HttpSession;
import java.io.ByteArrayInputStream;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import static top.zhangqianxi.constant.CommonConst.ARTICLE_SET;
import static top.zhangqianxi.constant.CommonConst.FALSE;
import static top.zhangqianxi.constant.RedisPrefixConst.*;
import static top.zhangqianxi.constant.RedisPrefixConst.ARTICLE_LIKE_COUNT;
import static top.zhangqianxi.constant.RedisPrefixConst.ARTICLE_USER_LIKE;
import static top.zhangqianxi.constant.RedisPrefixConst.ARTICLE_VIEWS_COUNT;
import static top.zhangqianxi.enums.ArticleStatusEnum.DRAFT;
import static top.zhangqianxi.enums.ArticleStatusEnum.PUBLIC;

/**
* @author 86152
* @description ????????????tb_article?????????????????????Service??????
* @createDate 2023-01-05 11:01:49
*/
@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements ArticleService{

    @Autowired
    private ArticleMapper articleMapper;
    @Autowired
    private RedisService redisService;
    @Autowired
    private BlogInfoService blogInfoService;
    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private ArticleTagMapper articleTagMapper;
    @Autowired
    private TagService tagService;
    @Autowired
    private TagMapper tagMapper;
    @Autowired
    private ArticleTagService articleTagService;
    @Autowired
    private HttpSession session;
    @Autowired
    private SearchStrategyContext searchStrategyContext;
    @Autowired
    private UploadStrategyContext uploadStrategyContext;

    @Override
    public PageResult<ArticleBackDTO> listArticleBacks(ConditionVO condition) {
        // ??????????????????
        Integer count = articleMapper.countArticleBacks(condition);
        if (count == 0) {
            return new PageResult<>();
        }
        // ??????????????????
        List<ArticleBackDTO> articleBackDTOList = articleMapper.listArticleBacks(PageUtils.getLimitCurrent(), PageUtils.getSize(), condition);
        // ?????????????????????????????????
        Map<Object, Double> viewsCountMap = redisService.zAllScore(ARTICLE_VIEWS_COUNT);
        Map<String, Object> likeCountMap = redisService.hGetAll(ARTICLE_LIKE_COUNT);
        // ???????????????????????????
        articleBackDTOList.forEach(item -> {
            Double viewsCount = viewsCountMap.get(item.getId());
            if (Objects.nonNull(viewsCount)) {
                item.setViewsCount(viewsCount.intValue());
            }
            item.setLikeCount((Integer) likeCountMap.get(item.getId().toString()));
        });
        return new PageResult<>(articleBackDTOList, count);
    }

    @Override
    public PageResult<ArchiveDTO> listArchives() {
        Page<Article> page = new Page<>(PageUtils.getCurrent(), PageUtils.getSize());
        // ??????????????????
        Page<Article> articlePage = articleMapper.selectPage(page, new LambdaQueryWrapper<Article>()
                .select(Article::getId, Article::getArticleTitle, Article::getCreateTime).orderByDesc(Article::getCreateTime)
                .eq(Article::getIsDelete, FALSE)
                .eq(Article::getStatus, PUBLIC.getStatus()));
        List<ArchiveDTO> archiveDTOList = BeanCopyUtils.copyList(articlePage.getRecords(), ArchiveDTO.class);
        return new PageResult<>(archiveDTOList, (int) articlePage.getTotal());
    }

    @Override
    public List<ArticleHomeDTO> listArticles() {
        return articleMapper.listArticles(PageUtils.getLimitCurrent(), PageUtils.getSize());
    }

    @Override
    public ArticlePreviewListDTO listArticlesByCondition(ConditionVO condition) {
        // ????????????
        List<ArticlePreviewDTO> articlePreviewDTOList = articleMapper.listArticlesByCondition(PageUtils.getLimitCurrent(), PageUtils.getSize(), condition);
        // ?????????????????????(??????????????????)
        String name;
        if (Objects.nonNull(condition.getCategoryId())) {
            name = categoryMapper.selectOne(new LambdaQueryWrapper<Category>().select(Category::getCategoryName)
                    .eq(Category::getId, condition.getCategoryId())).getCategoryName();
        } else {
            name = tagService.getOne(new LambdaQueryWrapper<Tag>()
                    .select(Tag::getTagName).eq(Tag::getId, condition.getTagId())).getTagName();
        }
        return ArticlePreviewListDTO.builder().articlePreviewDTOList(articlePreviewDTOList).name(name).build();
    }

    @Override
    public List<ArticleSearchDTO> listArticlesBySearch(ConditionVO condition) {
        return searchStrategyContext.executeSearchStrategy(condition.getKeywords());
    }

    @Override
    public ArticleVO getArticleBackById(Integer articleId) {
        // ??????????????????
        Article article = articleMapper.selectById(articleId);
        // ??????????????????
        Category category = categoryMapper.selectById(article.getCategoryId());
        String categoryName = null;
        if (Objects.nonNull(category)) {
            categoryName = category.getCategoryName();
        }
        // ??????????????????
        List<String> tagNameList = tagMapper.listTagNameByArticleId(articleId);
        // ????????????
        ArticleVO articleVO = BeanCopyUtils.copyObject(article, ArticleVO.class);
        articleVO.setCategoryName(categoryName);
        articleVO.setTagNameList(tagNameList);
        return articleVO;
    }

    @Override
    public ArticleDTO getArticleById(Integer articleId) {
        // ??????????????????
        CompletableFuture<List<ArticleRecommendDTO>> recommendArticleList = CompletableFuture.supplyAsync(() -> articleMapper.listRecommendArticles(articleId));
        // ??????????????????
        CompletableFuture<List<ArticleRecommendDTO>> newestArticleList = CompletableFuture.supplyAsync(() -> {
            List<Article> articleList = articleMapper.selectList(new LambdaQueryWrapper<Article>()
                    .select(Article::getId, Article::getArticleTitle, Article::getArticleCover, Article::getCreateTime).eq(Article::getIsDelete, FALSE)
                    .eq(Article::getStatus, PUBLIC.getStatus()).orderByDesc(Article::getId).last("limit 5"));
            return BeanCopyUtils.copyList(articleList, ArticleRecommendDTO.class);
        });
        // ??????id????????????
        ArticleDTO article = articleMapper.getArticleById(articleId);
        if (Objects.isNull(article)) {
            throw new BizException("???????????????");
        }
        // ?????????????????????
        updateArticleViewsCount(articleId);
        // ??????????????????????????????
        Article lastArticle = articleMapper.selectOne(new LambdaQueryWrapper<Article>()
                .select(Article::getId, Article::getArticleTitle, Article::getArticleCover).eq(Article::getIsDelete, FALSE)
                .eq(Article::getStatus, PUBLIC.getStatus())
                .lt(Article::getId, articleId)
                .orderByDesc(Article::getId).last("limit 1"));
        Article nextArticle = articleMapper.selectOne(new LambdaQueryWrapper<Article>()
                .select(Article::getId, Article::getArticleTitle, Article::getArticleCover).eq(Article::getIsDelete, FALSE)
                .eq(Article::getStatus, PUBLIC.getStatus())
                .gt(Article::getId, articleId).orderByAsc(Article::getId)
                .last("limit 1"));
        article.setLastArticle(BeanCopyUtils.copyObject(lastArticle, ArticlePaginationDTO.class));
        article.setNextArticle(BeanCopyUtils.copyObject(nextArticle, ArticlePaginationDTO.class));
        // ???????????????????????????
        Double score = redisService.zScore(ARTICLE_VIEWS_COUNT, articleId);
        if (Objects.nonNull(score)) {
            article.setViewsCount(score.intValue());
        }
        article.setLikeCount((Integer) redisService.hGet(ARTICLE_LIKE_COUNT, articleId.toString()));
        // ??????????????????
        try {
            article.setRecommendArticleList(recommendArticleList.get());
            article.setNewestArticleList(newestArticleList.get());
        } catch (Exception e) {
            log.error(StrUtil.format("????????????:{}", ExceptionUtil.stacktraceToString(e)));
        }
        return article;
    }

    /**
     * ?????????????????????
     *
     * @param articleId ??????id
     */
    public void updateArticleViewsCount(Integer articleId) {
        // ?????????????????????????????????????????????
        Set<Integer> articleSet = CommonUtils.castSet(Optional.ofNullable(session.getAttribute(ARTICLE_SET)).orElseGet(HashSet::new), Integer.class);
        if (!articleSet.contains(articleId)) {
            articleSet.add(articleId);
            session.setAttribute(ARTICLE_SET, articleSet);
            // ?????????+1
            redisService.zIncr(ARTICLE_VIEWS_COUNT, articleId, 1D);
        }
    }

    @Override
    public void saveArticleLike(Integer articleId) {
        // ??????????????????
        String articleLikeKey = ARTICLE_USER_LIKE + UserUtils.getLoginUser().getUserInfoId();
        if (redisService.sIsMember(articleLikeKey, articleId)) {
            // ????????????????????????id
            redisService.sRemove(articleLikeKey, articleId);
            // ???????????????-1
            redisService.hDecr(ARTICLE_LIKE_COUNT, articleId.toString(), 1L);
        } else {
            // ????????????????????????id
            redisService.sAdd(articleLikeKey, articleId);
            // ???????????????+1
            redisService.hIncr(ARTICLE_LIKE_COUNT, articleId.toString(), 1L);
        }
    }

    @Override
    public void saveOrUpdateArticle(ArticleVO articleVO) {
        // ????????????????????????
        CompletableFuture<WebsiteConfigVO> webConfig = CompletableFuture.supplyAsync(() -> blogInfoService.getWebsiteConfig());

        // ??????????????????
        Category category = saveArticleCategory(articleVO);
        // ?????????????????????
        Article article = BeanCopyUtils.copyObject(articleVO, Article.class);
        if (Objects.nonNull(category)) {
            article.setCategoryId(category.getId());
        }
        // ????????????????????????
        if (StrUtil.isBlank(article.getArticleCover())){
            try {
                article.setArticleCover(webConfig.get().getArticleCover());
            } catch (Exception e) {
                throw new BizException("??????????????????????????????");
            }
        }
        article.setUserId(UserUtils.getLoginUser().getUserInfoId());
        this.saveOrUpdate(article);
        // ??????????????????
        saveArticleTag(articleVO, article.getId());
    }

    /**
     * ??????????????????
     *
     * @param articleVO ????????????
     * @return {@link Category} ????????????
     */
    private Category saveArticleCategory(ArticleVO articleVO) {
        // ????????????????????????
        Category category = categoryMapper.selectOne(new LambdaQueryWrapper<Category>().eq(Category::getCategoryName, articleVO.getCategoryName()));
        if (Objects.isNull(category) && !articleVO.getStatus().equals(DRAFT.getStatus())) {
            category = Category.builder().categoryName(articleVO.getCategoryName()).build();
            categoryMapper.insert(category);
        }
        return category;
    }

    /**
     * ??????????????????
     *
     * @param articleVO ????????????
     */
    private void saveArticleTag(ArticleVO articleVO, Integer articleId) {
        // ???????????????????????????????????????
        if (Objects.nonNull(articleVO.getId())) {
            articleTagMapper.delete(new LambdaQueryWrapper<ArticleTag>().eq(ArticleTag::getArticleId, articleVO.getId()));
        }
        // ??????????????????
        List<String> tagNameList = articleVO.getTagNameList();
        if (CollectionUtils.isNotEmpty(tagNameList)) {
            // ????????????????????????
            List<Tag> existTagList = tagService.list(new LambdaQueryWrapper<Tag>().in(Tag::getTagName, tagNameList));
            List<String> existTagNameList = existTagList.stream().map(Tag::getTagName).collect(Collectors.toList());
            List<Integer> existTagIdList = existTagList.stream().map(Tag::getId).collect(Collectors.toList());
            // ??????????????????????????????
            tagNameList.removeAll(existTagNameList);
            if (CollectionUtils.isNotEmpty(tagNameList)) {
                List<Tag> tagList = tagNameList.stream().map(item -> Tag.builder().tagName(item).build()).collect(Collectors.toList());
                tagService.saveBatch(tagList);
                List<Integer> tagIdList = tagList.stream().map(Tag::getId).collect(Collectors.toList());
                existTagIdList.addAll(tagIdList);
            }
            // ????????????id????????????
            List<ArticleTag> articleTagList = existTagIdList.stream().map(item -> ArticleTag.builder()
                            .articleId(articleId)
                            .tagId(item)
                            .build())
                    .collect(Collectors.toList());
            articleTagService.saveBatch(articleTagList);
        }
    }

    @Override
    public void updateArticleTop(ArticleTopVO articleTopVO) {
        // ????????????????????????
        Article article = Article.builder().id(articleTopVO.getId()).isTop(articleTopVO.getIsTop()).build();
        articleMapper.updateById(article);
    }

    @Override
    public void updateArticleDelete(DeleteVO deleteVO) {
        // ??????????????????????????????
        List<Article> articleList = deleteVO.getIdList().stream().map(id -> Article.builder()
                        .id(id)
                        .isTop(FALSE)
                        .isDelete(deleteVO.getIsDelete())
                        .build())
                .collect(Collectors.toList());
        this.updateBatchById(articleList);
    }

    @Override
    public void deleteArticles(List<Integer> articleIdList) {
        // ????????????????????????
        articleTagMapper.delete(new LambdaQueryWrapper<ArticleTag>().in(ArticleTag::getArticleId, articleIdList));
        // ????????????
        articleMapper.deleteBatchIds(articleIdList);
    }

    @Override
    public List<String> exportArticles(List<Integer> articleIdList) {
        // ??????????????????
        List<Article> articleList = articleMapper.selectList(new LambdaQueryWrapper<Article>()
                .select(Article::getArticleTitle, Article::getArticleContent)
                .in(Article::getId, articleIdList));
        // ?????????????????????
        List<String> urlList = new ArrayList<>();
        for (Article article : articleList) {
            try (ByteArrayInputStream inputStream = new ByteArrayInputStream(article.getArticleContent().getBytes())) {
                String url = uploadStrategyContext.executeUploadStrategy(article.getArticleTitle() + FileExtEnum.MD.getExtName(), inputStream, FilePathEnum.MD.getPath());
                urlList.add(url);
            } catch (Exception e) {
                log.error(StrUtil.format("??????????????????,??????:{}", ExceptionUtil.stacktraceToString(e)));
                throw new BizException("??????????????????");
            }
        }
        return urlList;
    }

}




