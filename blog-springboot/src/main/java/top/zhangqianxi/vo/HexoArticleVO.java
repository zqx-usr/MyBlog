package top.zhangqianxi.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * ClassName: HexoArticleVO
 * Description: hexo文章
 *
 * @Author: zhangqianxi
 * @Create: 2023/1/5 10:23
 * @Version: V1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class HexoArticleVO extends ArticleVO {
    private LocalDateTime createTime;
}
