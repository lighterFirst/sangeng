package com.cheng;

import com.cheng.service.ArticleTagService;
import com.cheng.service.MenuService;
import com.cheng.utils.SecurityUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class test01 {

    @Autowired
    private ArticleTagService articleTagService;

    @Autowired
    private MenuService menuService;


    @Test
    public void test02(){
        System.out.println(articleTagService.getTagId((long)9));
    }

    @Test
    public void test03(){
        System.out.println(menuService.deleteMenu((long) 1));
    }


    @Test
    public void test04(){
        Long id = (long)5;
        System.out.println(id != null && id.equals(1L));

    }
}
