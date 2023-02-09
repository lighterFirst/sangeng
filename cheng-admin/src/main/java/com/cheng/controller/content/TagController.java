package com.cheng.controller.content;

import com.cheng.domain.ResponseResult;
import com.cheng.domain.dto.TagListDto;
import com.cheng.domain.dto.TagUpdateDto;
import com.cheng.domain.entity.Tag;
import com.cheng.service.TagService;
import com.cheng.vo.PageVo;
import com.cheng.vo.TagVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/content/tag")
public class TagController {

    @Autowired
    private TagService tagService;

    //分页查询标签
    @GetMapping("/list")
    public ResponseResult<PageVo> list(long pageNum, long pageSize, TagListDto tagListDto){

        return tagService.getList(pageNum,pageSize,tagListDto);
    }

    //新增标签
    @PostMapping
    public ResponseResult<Tag> addTag(@RequestBody Tag tag){
        return tagService.addTag(tag);
    }

    /**
     * 删除标签
     * @param id
     * @return
     */

    @DeleteMapping("/{id}")
    public ResponseResult deleteTag(@PathVariable("id") long id){
        return tagService.deleteTag(id);
    }

    //修改标签有两个接口

    /**
     * 回显修改的数据
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public ResponseResult queryTag(@PathVariable("id") long id){

        return tagService.queryTag(id);

    }

    @PutMapping
    public ResponseResult updateTag(@RequestBody TagUpdateDto tagUpdateDto){
        return tagService.updateTag(tagUpdateDto);
    }

    @GetMapping("/listAllTag")
    public ResponseResult<TagVo> listAllTag(){
        return tagService. listAllTag();
    }
}
