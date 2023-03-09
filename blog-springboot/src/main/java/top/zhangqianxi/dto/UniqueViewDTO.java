package top.zhangqianxi.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
* ClassName: UniqueViewDTO
* Description: 访问量
*
*/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UniqueViewDTO {

   /**
    * 日期
    */
   private String day;

   /**
    * 访问量
    */
   private Integer viewsCount;

}
