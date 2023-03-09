package top.zhangqianxi.strategy.impl;

import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;
import top.zhangqianxi.exception.BizException;
import top.zhangqianxi.service.ArticleService;
import top.zhangqianxi.strategy.ArticleImportStrategy;
import top.zhangqianxi.vo.HexoArticleVO;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static top.zhangqianxi.constant.HexoConst.*;
import static top.zhangqianxi.constant.HexoConst.CATEGORIES_PREFIX;
import static top.zhangqianxi.constant.HexoConst.CATEGORY_FLAG;
import static top.zhangqianxi.constant.HexoConst.DATE_PREFIX;
import static top.zhangqianxi.constant.HexoConst.DELIMITER;
import static top.zhangqianxi.constant.HexoConst.NEW_LINE;
import static top.zhangqianxi.constant.HexoConst.NORMAL_FLAG;
import static top.zhangqianxi.constant.HexoConst.PREFIX;
import static top.zhangqianxi.constant.HexoConst.TAGS_PREFIX;
import static top.zhangqianxi.constant.HexoConst.TAG_FLAG;
import static top.zhangqianxi.constant.HexoConst.TITLE_PREFIX;
import static top.zhangqianxi.enums.ArticleStatusEnum.DRAFT;
import static top.zhangqianxi.enums.ArticleStatusEnum.PUBLIC;
import static top.zhangqianxi.enums.ArticleTypeEnum.ORIGINAL;

/**
 * ClassName: HexoArticleImportStrategyImpl
 * Description: Hexo文章导入策略
 *
 * @Author: zhangqianxi
 * @Create: 2023/1/6 12:41
 * @Version: V1.0
 */
@Slf4j
@Service("hexoArticleImportStrategyImpl")
public class HexoArticleImportStrategyImpl implements ArticleImportStrategy {

    @Autowired
    private ArticleService articleService;

    /**
     * hexo最大分隔符数
     */
    private final int HEXO_MAX_DELIMITER_COUNT = 2;

    /**
     * hexo最小分隔符数
     */
    private final int HEXO_MIN_DELIMITER_COUNT = 1;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public void importArticles(MultipartFile file) {
        try {
            HexoArticleVO hexoArticleVO = new HexoArticleVO();
            // 原创
            hexoArticleVO.setType(ORIGINAL.getType());
            // 公开 （DRAFT不保存分类）
            hexoArticleVO.setStatus(PUBLIC.getStatus());

            AtomicInteger hexoDelimiterCount = new AtomicInteger();
            StringBuilder articleContent = new StringBuilder();

            // 分类或标签标记
            AtomicInteger flag = new AtomicInteger(NORMAL_FLAG);

            List<String> tagList = new ArrayList<>();

            BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8));
            reader.lines().forEach(line -> {
                if (hexoDelimiterCount.get() == HEXO_MAX_DELIMITER_COUNT) {
                    // 分隔符结束就是正文
                    articleContent.append(line).append(NEW_LINE);
                } else {
                    if (line.equals(DELIMITER)) {
                        hexoDelimiterCount.getAndIncrement();
                    }
                    if (hexoDelimiterCount.get() == HEXO_MIN_DELIMITER_COUNT) {
                        if (line.startsWith(TITLE_PREFIX)) {
                            hexoArticleVO.setArticleTitle(line.replace(TITLE_PREFIX, "").trim());
                        } else if (line.startsWith(DATE_PREFIX)) {
                            hexoArticleVO.setCreateTime(LocalDateTime.parse(line.replace(DATE_PREFIX, "").trim(), formatter));
                        } else if (line.startsWith(CATEGORIES_PREFIX)) {
                            flag.set(CATEGORY_FLAG);
                        } else if (line.startsWith(TAGS_PREFIX)) {
                            flag.set(TAG_FLAG);
                        } else if (line.startsWith(PREFIX) && flag.intValue() == CATEGORY_FLAG) {
                            hexoArticleVO.setCategoryName(line.replace(PREFIX, "").trim());
                        } else if (line.startsWith(PREFIX) && flag.intValue() == TAG_FLAG) {
                            tagList.add(line.replace(PREFIX, "").trim());
                        }
                    }
                }
            });

            hexoArticleVO.setTagNameList(tagList);
            hexoArticleVO.setArticleContent(articleContent.toString());

            // 如果分类或标签为空则设为草稿
            if (CollectionUtils.isEmpty(hexoArticleVO.getTagNameList()) || StrUtil.isBlank(hexoArticleVO.getCategoryName())) {
                hexoArticleVO.setStatus(DRAFT.getStatus());
            }

            articleService.saveOrUpdateArticle(hexoArticleVO);
        } catch (IOException e) {
            log.error(StrUtil.format("导入Hexo文章失败, 堆栈:{}", ExceptionUtil.stacktraceToString(e)));
            throw new BizException("导入Hexo文章失败");
        }
    }
}
