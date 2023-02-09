package com.cheng.utils;

import com.cheng.domain.entity.Article;
import com.cheng.vo.HotArticleVo;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;

public class BeanCopyUtils {

    private BeanCopyUtils() {

    }

    //方法一用于返回是单个对象

    public static <V> V copyBean(Object source, Class<V> clazz){

        V result = null;

        //创建目标对象

        try {
            result = clazz.newInstance();
            BeanUtils.copyProperties(source,result);
            return result;
        }catch (Exception e){
            e.printStackTrace();
        }

        return result;
    }

    //方法值用于返回是个对象集合
    public static <T> List<T> copyBeanList(List<?> source, Class<T> clazz){

        T result = null;

        List<T> list =new ArrayList<>();

        for(Object object:source){

            //创建目标对象

            try {
                result = clazz.newInstance();
                BeanUtils.copyProperties(object,result);
                list.add(result);
            }catch (Exception e){
                e.printStackTrace();
            }

        }


        return list;
    }



    public static void main(String[] args) {
        Article article = new Article();
        System.out.println(BeanCopyUtils.copyBean(article,HotArticleVo.class));
    }

}
