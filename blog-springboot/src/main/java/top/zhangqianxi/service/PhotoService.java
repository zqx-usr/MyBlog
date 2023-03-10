package top.zhangqianxi.service;

import top.zhangqianxi.dto.PhotoBackDTO;
import top.zhangqianxi.dto.PhotoDTO;
import top.zhangqianxi.entity.Photo;
import com.baomidou.mybatisplus.extension.service.IService;
import top.zhangqianxi.vo.*;

import java.util.List;

/**
* @author 86152
* @description 针对表【tb_photo(照片)】的数据库操作Service
* @createDate 2023-01-05 11:03:38
*/
public interface PhotoService extends IService<Photo> {

    /**
     * 根据相册id获取照片列表
     *
     * @param condition 条件
     * @return {@link PageResult<PhotoBackDTO>} 照片列表
     */
    PageResult<PhotoBackDTO> listPhotos(ConditionVO condition);

    /**
     * 更新照片信息
     *
     * @param photoInfoVO 照片信息
     */
    void updatePhoto(PhotoInfoVO photoInfoVO);

    /**
     * 保存照片
     *
     * @param photoVO 照片
     */
    void savePhotos(PhotoVO photoVO);

    /**
     * 移动照片相册
     *
     * @param photoVO 照片信息
     */
    void updatePhotosAlbum(PhotoVO photoVO);

    /**
     * 更新照片删除状态
     *
     * @param deleteVO 删除信息
     */
    void updatePhotoDelete(DeleteVO deleteVO);

    /**
     * 删除照片
     *
     * @param photoIdList 照片id列表
     */
    void deletePhotos(List<Integer> photoIdList);

    /**
     * 根据相册id查看照片列表
     *
     * @param albumId 相册id
     * @return {@link List <PhotoDTO>} 照片列表
     */
    PhotoDTO listPhotosByAlbumId(Integer albumId);
}
