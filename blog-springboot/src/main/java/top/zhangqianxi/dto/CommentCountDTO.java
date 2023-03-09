package top.zhangqianxi.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
* ClassName: CommentCountDTO
* Description: 搜索文章
*
*/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentCountDTO {

   /**
    * id
    */
   private Integer id;

   /**
    * 评论数量
    */
   private Integer commentCount;
}
