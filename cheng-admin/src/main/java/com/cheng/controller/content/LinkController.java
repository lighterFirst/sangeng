package com.cheng.controller.content;

import com.cheng.domain.ResponseResult;
import com.cheng.domain.dto.AddLinkDto;
import com.cheng.domain.entity.Link;
import com.cheng.service.LinkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/content/link")
public class LinkController {

    @Autowired
    private LinkService linkService;

    @GetMapping("/list")
    public ResponseResult<Link> listLink(Long pageNum, Long pageSize,Link link){
        return linkService.listLink(pageNum,pageSize,link);
    }

    @PostMapping
    public ResponseResult<Link> addLink(@RequestBody AddLinkDto addLinkDto){
        return linkService.addLink(addLinkDto);
    }

    //修改友链

    @GetMapping("/{id}")
    public ResponseResult showLink(@PathVariable Long id){
        return linkService.showLink(id);
    }

    @PutMapping
    public ResponseResult updateLink(@RequestBody Link link){
        return linkService.updateLink(link);
    }

    @DeleteMapping("/{id}")
    public ResponseResult deleteLink(@PathVariable Long id){
        return linkService.deleteLink(id);
    }

}
