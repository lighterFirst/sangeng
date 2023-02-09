package com.cheng.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.cheng.utils.SecurityUtils;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * mybatisplus的自动填充
 */

@Component
public class MyMetaObjectHandler implements MetaObjectHandler {

    Long userId = null;

    @Override
    public void insertFill(MetaObject metaObject) {


        /**
         * 注册用户会出现问题
         */
       /* try {
            userId = SecurityUtils.getUserId();
        } catch (Exception e) {
            e.printStackTrace();
            userId = -1L;//表示是自己创建
        }*/
        userId = SecurityUtils.getUserId();

        this.strictInsertFill(metaObject,"createTime",()->new Date(),Date.class);
        this.strictInsertFill(metaObject,"updateTime",()->new Date(),Date.class);
        this.strictInsertFill(metaObject,"updateBy",()-> userId,Long.class);
        this.strictInsertFill(metaObject,"createBy",()-> userId,Long.class);
    }

    @Override
    public void updateFill(MetaObject metaObject) {

        this.strictInsertFill(metaObject,"updateTime",()->new Date(),Date.class);
        this.strictInsertFill(metaObject,"updateBy",()-> userId,Long.class);
    }
}
