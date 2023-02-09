package com.cheng.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cheng.domain.ResponseResult;
import com.cheng.domain.dto.TagListDto;
import com.cheng.domain.dto.TagUpdateDto;
import com.cheng.service.TagService;
import com.cheng.domain.entity.Tag;
import com.cheng.mapper.TagMapper;
import com.cheng.utils.BeanCopyUtils;
import com.cheng.vo.PageVo;
import com.cheng.vo.TagVo;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 标签(Tag)表服务实现类
 *
 * @author cheng
 * @since 2022-12-22 14:12:36
 */
@Service("tagService")
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag> implements TagService {

    @Override
    public ResponseResult getList(long pageNum, long pageSize, TagListDto tagListDto) {

        LambdaQueryWrapper<Tag> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(StringUtils.hasText(tagListDto.getName()),Tag::getName,tagListDto.getName());
        lambdaQueryWrapper.eq(StringUtils.hasText(tagListDto.getRemark()),Tag::getRemark,tagListDto.getRemark());
        Page<Tag> page = new Page<>(pageNum,pageSize);
        page(page,lambdaQueryWrapper);
        List<Tag> tag = page.getRecords();


        return ResponseResult.okResult(new PageVo(tag,page.getTotal()));
    }

    @Override
    public ResponseResult<Tag> addTag(Tag tag) {
        Tag newTag = new Tag();
        newTag.setName(tag.getName());
        newTag.setRemark(tag.getRemark());
        save(newTag);


        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult deleteTag(long id) {

        removeById(id);

        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult queryTag(long id) {
        Tag tag = getById(id);
        TagVo tagVo = BeanCopyUtils.copyBean(tag, TagVo.class);
        return ResponseResult.okResult(tagVo);
    }

    @Override
    public ResponseResult updateTag(TagUpdateDto tagUpdateDto) {
        Tag tag = BeanCopyUtils.copyBean(tagUpdateDto, Tag.class);
        updateById(tag);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult<TagVo> listAllTag() {
        LambdaQueryWrapper<Tag> wrapper = new LambdaQueryWrapper<>();
        wrapper.select(Tag::getId,Tag::getName);
        List<Tag> list = list(wrapper);
        List<TagVo> tagVos = BeanCopyUtils.copyBeanList(list, TagVo.class);
        return ResponseResult.okResult(tagVos);
    }
}

