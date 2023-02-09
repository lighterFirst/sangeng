package com.cheng.controller;

import com.cheng.domain.ResponseResult;
import com.cheng.service.LinkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 友链
 */

@RestController
@RequestMapping("/link")
public class LinkController {

    @Autowired
    private LinkService linkService;


    /**
     * 查询所有审核通过的友链
     * @return
     */
    @GetMapping("/getAllLink")
    public ResponseResult test01(){
        return linkService.getAllLink();
    }



}
