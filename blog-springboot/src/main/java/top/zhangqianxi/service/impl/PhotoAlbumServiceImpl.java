package top.zhangqianxi.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import top.zhangqianxi.dto.PhotoAlbumBackDTO;
import top.zhangqianxi.dto.PhotoAlbumDTO;
import top.zhangqianxi.entity.Photo;
import top.zhangqianxi.entity.PhotoAlbum;
import top.zhangqianxi.exception.BizException;
import top.zhangqianxi.mapper.PhotoMapper;
import top.zhangqianxi.service.PhotoAlbumService;
import top.zhangqianxi.mapper.PhotoAlbumMapper;
import org.springframework.stereotype.Service;
import top.zhangqianxi.util.BeanCopyUtils;
import top.zhangqianxi.util.PageUtils;
import top.zhangqianxi.vo.ConditionVO;
import top.zhangqianxi.vo.PageResult;
import top.zhangqianxi.vo.PhotoAlbumVO;

import java.util.List;
import java.util.Objects;

import static top.zhangqianxi.constant.CommonConst.FALSE;
import static top.zhangqianxi.constant.CommonConst.TRUE;
import static top.zhangqianxi.enums.PhotoAlbumStatusEnum.PUBLIC;

/**
* @author 86152
* @description 针对表【tb_photo_album(相册)】的数据库操作Service实现
* @createDate 2023-01-05 11:03:46
*/
@Service
public class PhotoAlbumServiceImpl extends ServiceImpl<PhotoAlbumMapper, PhotoAlbum> implements PhotoAlbumService{
    @Autowired
    private PhotoAlbumMapper photoAlbumMapper;
    @Autowired
    private PhotoMapper photoMapper;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void saveOrUpdatePhotoAlbum(PhotoAlbumVO photoAlbumVO) {
        // 查询相册名是否存在
        PhotoAlbum album = photoAlbumMapper.selectOne(new LambdaQueryWrapper<PhotoAlbum>()
                .select(PhotoAlbum::getId)
                .eq(PhotoAlbum::getAlbumName, photoAlbumVO.getAlbumName()));
        if (Objects.nonNull(album) && !album.getId().equals(photoAlbumVO.getId())) {
            throw new BizException("相册名已存在");
        }
        PhotoAlbum photoAlbum = BeanCopyUtils.copyObject(photoAlbumVO, PhotoAlbum.class);
        this.saveOrUpdate(photoAlbum);
    }

    @Override
    public PageResult<PhotoAlbumBackDTO> listPhotoAlbumBacks(ConditionVO condition) {
        // 查询相册数量
        Integer count = Math.toIntExact(photoAlbumMapper.selectCount(new LambdaQueryWrapper<PhotoAlbum>()
                .like(StringUtils.isNotBlank(condition.getKeywords()), PhotoAlbum::getAlbumName, condition.getKeywords())
                .eq(PhotoAlbum::getIsDelete, FALSE)));
        if (count == 0) {
            return new PageResult<>();
        }
        // 查询相册信息
        List<PhotoAlbumBackDTO> photoAlbumBackList = photoAlbumMapper.listPhotoAlbumBacks(PageUtils.getLimitCurrent(), PageUtils.getSize(), condition);
        return new PageResult<>(photoAlbumBackList, count);
    }

    @Override
    public List<PhotoAlbumDTO> listPhotoAlbumBackInfos() {
        List<PhotoAlbum> photoAlbumList = photoAlbumMapper.selectList(new LambdaQueryWrapper<PhotoAlbum>()
                .eq(PhotoAlbum::getIsDelete, FALSE));
        return BeanCopyUtils.copyList(photoAlbumList, PhotoAlbumDTO.class);
    }

    @Override
    public PhotoAlbumBackDTO getPhotoAlbumBackById(Integer albumId) {
        // 查询相册信息
        PhotoAlbum photoAlbum = photoAlbumMapper.selectById(albumId);
        // 查询照片数量
        Integer photoCount = Math.toIntExact(photoMapper.selectCount(new LambdaQueryWrapper<Photo>()
                .eq(Photo::getAlbumId, albumId)
                .eq(Photo::getIsDelete, FALSE)));
        PhotoAlbumBackDTO album = BeanCopyUtils.copyObject(photoAlbum, PhotoAlbumBackDTO.class);
        album.setPhotoCount(photoCount);
        return album;
    }

    @Override
    public void deletePhotoAlbumById(Integer albumId) {
        // 查询照片数量
        Integer count = Math.toIntExact(photoMapper.selectCount(new LambdaQueryWrapper<Photo>()
                .eq(Photo::getAlbumId, albumId)));
        if (count > 0) {
            // 若相册下存在照片则逻辑删除相册和照片
            photoAlbumMapper.updateById(PhotoAlbum.builder()
                    .id(albumId)
                    .isDelete(TRUE)
                    .build());
            photoMapper.update(new Photo(), new LambdaUpdateWrapper<Photo>()
                    .set(Photo::getIsDelete, TRUE)
                    .eq(Photo::getAlbumId, albumId));
        } else {
            // 若相册下不存在照片则直接删除
            photoAlbumMapper.deleteById(albumId);
        }
    }

    @Override
    public List<PhotoAlbumDTO> listPhotoAlbums() {
        // 查询相册列表
        List<PhotoAlbum> photoAlbumList = photoAlbumMapper.selectList(new LambdaQueryWrapper<PhotoAlbum>()
                .eq(PhotoAlbum::getStatus, PUBLIC.getStatus())
                .eq(PhotoAlbum::getIsDelete, FALSE)
                .orderByDesc(PhotoAlbum::getId));
        return BeanCopyUtils.copyList(photoAlbumList, PhotoAlbumDTO.class);
    }
}




