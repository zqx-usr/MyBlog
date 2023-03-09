package top.zhangqianxi.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
* ClassName: FriendLinkBackDTO
* Description: 后台友情链接
*
*/
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FriendLinkBackDTO {
   /**
    * id
    */
   private Integer id;

   /**
    * 链接名
    */
   private String linkName;

   /**
    * 链接头像
    */
   private String linkAvatar;

   /**
    * 链接地址
    */
   private String linkAddress;

   /**
    * 链接介绍
    */
   private String linkIntro;

   /**
    * 创建时间
    */
   private LocalDateTime createTime;

}