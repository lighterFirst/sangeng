package com.cheng.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cheng.domain.ResponseResult;
import com.cheng.domain.dto.TagListDto;
import com.cheng.domain.dto.TagUpdateDto;
import com.cheng.domain.entity.Tag;
import com.cheng.vo.TagVo;

import java.util.List;

/**
 * 标签(Tag)表服务接口
 *
 * @author cheng
 * @since 2022-12-22 14:12:36
 */
public interface TagService extends IService<Tag> {

    ResponseResult getList(long pageNum, long pageSize, TagListDto tagListDto);

    ResponseResult<Tag> addTag(Tag tag);

    ResponseResult deleteTag(long id);

    ResponseResult queryTag(long id);

    ResponseResult updateTag(TagUpdateDto tagUpdateDto);

    ResponseResult<TagVo> listAllTag();
}

