package top.zhangqianxi.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
* ClassName: PhotoAlbumDTO
* Description: 相册
*
*/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PhotoAlbumDTO {

   /**
    * 相册id
    */
   private Integer id;

   /**
    * 相册名
    */
   private String albumName;

   /**
    * 相册描述
    */
   private String albumDesc;

   /**
    * 相册封面
    */
   private String albumCover;

}
