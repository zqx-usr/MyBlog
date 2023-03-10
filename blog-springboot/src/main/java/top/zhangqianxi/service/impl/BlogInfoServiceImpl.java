package top.zhangqianxi.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import eu.bitwalker.useragentutils.Browser;
import eu.bitwalker.useragentutils.OperatingSystem;
import eu.bitwalker.useragentutils.UserAgent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import top.zhangqianxi.dto.*;
import top.zhangqianxi.entity.Article;
import top.zhangqianxi.entity.UserInfo;
import top.zhangqianxi.entity.WebsiteConfig;
import top.zhangqianxi.mapper.*;
import top.zhangqianxi.service.BlogInfoService;
import top.zhangqianxi.service.PageService;
import top.zhangqianxi.service.RedisService;
import top.zhangqianxi.service.UniqueViewService;
import top.zhangqianxi.util.BeanCopyUtils;
import top.zhangqianxi.util.IpUtils;
import top.zhangqianxi.vo.BlogInfoVO;
import top.zhangqianxi.vo.PageVO;
import top.zhangqianxi.vo.WebsiteConfigVO;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import java.util.*;
import java.util.stream.Collectors;

import static top.zhangqianxi.constant.CommonConst.*;
import static top.zhangqianxi.constant.CommonConst.CITY;
import static top.zhangqianxi.constant.CommonConst.DEFAULT_CONFIG_ID;
import static top.zhangqianxi.constant.CommonConst.FALSE;
import static top.zhangqianxi.constant.CommonConst.PROVINCE;
import static top.zhangqianxi.constant.CommonConst.UNKNOWN;
import static top.zhangqianxi.constant.RedisPrefixConst.*;
import static top.zhangqianxi.constant.RedisPrefixConst.ABOUT;
import static top.zhangqianxi.constant.RedisPrefixConst.ARTICLE_VIEWS_COUNT;
import static top.zhangqianxi.constant.RedisPrefixConst.BLOG_VIEWS_COUNT;
import static top.zhangqianxi.constant.RedisPrefixConst.UNIQUE_VISITOR;
import static top.zhangqianxi.constant.RedisPrefixConst.VISITOR_AREA;
import static top.zhangqianxi.constant.RedisPrefixConst.WEBSITE_CONFIG;
import static top.zhangqianxi.enums.ArticleStatusEnum.PUBLIC;

/**
 * ClassName: BlogInfoServiceImpl
 * Description: ???????????????????????????
 *
 * @Author: zhangqianxi
 * @Create: 2023/1/5 9:49
 * @Version: V1.0
 */
@Service
public class BlogInfoServiceImpl implements BlogInfoService {

    @Resource
    private HttpServletRequest request;
    @Autowired
    private RedisService redisService;
    @Autowired
    private MessageMapper messageMapper;
    @Autowired
    private UserInfoMapper userInfoMapper;
    @Autowired
    private ArticleMapper articleMapper;
    @Autowired
    private UniqueViewService uniqueViewService;
    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private TagMapper tagMapper;
    @Autowired
    private WebsiteConfigMapper websiteConfigMapper;
    @Autowired
    private PageService pageService;


    @Override
    public BlogHomeInfoDTO getBlogHomeInfo() {
        // ??????????????????
        Integer articleCount = Math.toIntExact(articleMapper.selectCount(new LambdaQueryWrapper<Article>()
                .eq(Article::getStatus, PUBLIC.getStatus())
                .eq(Article::getIsDelete, FALSE)));
        // ??????????????????
        Integer categoryCount = Math.toIntExact(categoryMapper.selectCount(null));
        // ??????????????????
        Integer tagCount = Math.toIntExact(tagMapper.selectCount(null));
        // ???????????????
        Object count = redisService.get(BLOG_VIEWS_COUNT);
        String viewsCount = Optional.ofNullable(count).orElse(0).toString();
        // ??????????????????
        WebsiteConfigVO websiteConfig = this.getWebsiteConfig();
        // ??????????????????
        List<PageVO> pageVOList = pageService.listPages();
        // ????????????
        return BlogHomeInfoDTO.builder()
                .articleCount(articleCount)
                .categoryCount(categoryCount)
                .tagCount(tagCount)
                .viewsCount(viewsCount)
                .websiteConfig(websiteConfig)
                .pageList(pageVOList)
                .build();
    }

    @Override
    public BlogBackInfoDTO getBlogBackInfo() {
        // ???????????????
        Object count = redisService.get(BLOG_VIEWS_COUNT);
        Integer viewsCount = Integer.parseInt(Optional.ofNullable(count).orElse(0).toString());
        // ???????????????
        Integer messageCount = Math.toIntExact(messageMapper.selectCount(null));
        // ???????????????
        Integer userCount = Math.toIntExact(userInfoMapper.selectCount(null));
        // ???????????????
        Integer articleCount = Math.toIntExact(articleMapper.selectCount(new LambdaQueryWrapper<Article>()
                .eq(Article::getIsDelete, FALSE)));
        // ?????????????????????
        List<UniqueViewDTO> uniqueViewList = uniqueViewService.listUniqueViews();
        // ??????????????????
        List<ArticleStatisticsDTO> articleStatisticsList = articleMapper.listArticleStatistics();
        // ??????????????????
        List<CategoryDTO> categoryDTOList = categoryMapper.listCategoryDTO();
        // ??????????????????
        List<TagDTO> tagDTOList = BeanCopyUtils.copyList(tagMapper.selectList(null), TagDTO.class);
        // ??????redis????????????????????????
        Map<Object, Double> articleMap = redisService.zReverseRangeWithScore(ARTICLE_VIEWS_COUNT, 0, 4);
        BlogBackInfoDTO blogBackInfoDTO = BlogBackInfoDTO.builder()
                .articleStatisticsList(articleStatisticsList)
                .tagDTOList(tagDTOList)
                .viewsCount(viewsCount)
                .messageCount(messageCount)
                .userCount(userCount)
                .articleCount(articleCount)
                .categoryDTOList(categoryDTOList)
                .uniqueViewDTOList(uniqueViewList)
                .build();
        if (CollectionUtils.isNotEmpty(articleMap)) {
            // ??????????????????
            List<ArticleRankDTO> articleRankDTOList = listArticleRank(articleMap);
            blogBackInfoDTO.setArticleRankDTOList(articleRankDTOList);
        }
        return blogBackInfoDTO;
    }

    /**
     * ??????????????????
     *
     * @param articleMap ????????????
     * @return {@link List<ArticleRankDTO>} ????????????
     */
    private List<ArticleRankDTO> listArticleRank(Map<Object, Double> articleMap) {
        // ????????????id
        List<Integer> articleIdList = new ArrayList<>(articleMap.size());
        articleMap.forEach((key, value) -> articleIdList.add((Integer) key));
        // ??????????????????
        return articleMapper.selectList(new LambdaQueryWrapper<Article>()
                        .select(Article::getId, Article::getArticleTitle)
                        .in(Article::getId, articleIdList))
                .stream().map(article -> ArticleRankDTO.builder()
                        .articleTitle(article.getArticleTitle())
                        .viewsCount(articleMap.get(article.getId()).intValue())
                        .build())
                .sorted(Comparator.comparingInt(ArticleRankDTO::getViewsCount).reversed())
                .collect(Collectors.toList());
    }

    @Override
    public void updateWebsiteConfig(WebsiteConfigVO websiteConfigVO) {
        // ??????????????????
        WebsiteConfig websiteConfig = WebsiteConfig.builder()
                .id(1)
                .config(JSON.toJSONString(websiteConfigVO))
                .build();
        websiteConfigMapper.updateById(websiteConfig);
        // ????????????
        redisService.del(WEBSITE_CONFIG);
    }

    @Override
    public WebsiteConfigVO getWebsiteConfig() {
        WebsiteConfigVO websiteConfigVO;
        // ??????????????????
        Object websiteConfig = redisService.get(WEBSITE_CONFIG);
        if (Objects.nonNull(websiteConfig)) {
            websiteConfigVO = JSON.parseObject(websiteConfig.toString(), WebsiteConfigVO.class);
        } else {
            // ?????????????????????
            String config = websiteConfigMapper.selectById(DEFAULT_CONFIG_ID).getConfig();
            websiteConfigVO = JSON.parseObject(config, WebsiteConfigVO.class);
            redisService.set(WEBSITE_CONFIG, config);
        }
        return websiteConfigVO;
    }

    @Override
    public String getAbout() {
        Object value = redisService.get(ABOUT);
        return Objects.nonNull(value) ? value.toString() : "";
    }

    @Override
    public void updateAbout(BlogInfoVO blogInfoVO) {
        redisService.set(ABOUT, blogInfoVO.getAboutContent());
    }

    @Override
    public void report() {
        // ??????ip
        String ipAddress = IpUtils.getIpAddress(request);
        // ??????????????????
        UserAgent userAgent = IpUtils.getUserAgent(request);
        Browser browser = userAgent.getBrowser();
        OperatingSystem operatingSystem = userAgent.getOperatingSystem();
        // ????????????????????????
        String uuid = ipAddress + browser.getName() + operatingSystem.getName();
        String md5 = DigestUtils.md5DigestAsHex(uuid.getBytes());
        // ??????????????????
        if (!redisService.sIsMember(UNIQUE_VISITOR, md5)) {
            // ????????????????????????
            String ipSource = IpUtils.getIpSource(ipAddress);
            if (StringUtils.isNotBlank(ipSource)) {
                ipSource = ipSource.substring(0, 2)
                        .replaceAll(PROVINCE, "")
                        .replaceAll(CITY, "");
                redisService.hIncr(VISITOR_AREA, ipSource, 1L);
            } else {
                redisService.hIncr(VISITOR_AREA, UNKNOWN, 1L);
            }
            // ?????????+1
            redisService.incr(BLOG_VIEWS_COUNT, 1);
            // ??????????????????
            redisService.sAdd(UNIQUE_VISITOR, md5);
        }
    }
}
