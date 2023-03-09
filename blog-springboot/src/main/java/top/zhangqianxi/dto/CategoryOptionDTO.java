package top.zhangqianxi.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
* ClassName: CategoryOptionDTO
* Description: 分类选项
*
*/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CategoryOptionDTO {

   /**
    * 分类id
    */
   private Integer id;

   /**
    * 分类名
    */
   private String categoryName;

}
