package top.zhangqianxi.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
* ClassName: UserAreaDTO
* Description: 用户地区
*
*/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserAreaDTO {

   /**
    * 地区名
    */
   private String name;

   /**
    * 数量
    */
   private Long value;

}
