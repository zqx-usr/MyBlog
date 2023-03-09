package top.zhangqianxi.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;


/**
* ClassName: TalkBackDTO
* Description: 后台说说
*
*/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TalkBackDTO {

   /**
    * 说说id
    */
   private Integer id;

   /**
    * 昵称
    */
   private String nickname;

   /**
    * 头像
    */
   private String avatar;

   /**
    * 说说内容
    */
   private String content;

   /**
    * 图片
    */
   private String images;

   /**
    * 图片列表
    */
   private List<String> imgList;

   /**
    * 是否置顶
    */
   private Integer isTop;

   /**
    * 状态
    */
   private Integer status;

   /**
    * 创建时间
    */
   private LocalDateTime createTime;

}
