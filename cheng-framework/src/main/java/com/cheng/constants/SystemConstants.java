package com.cheng.constants;


public class SystemConstants
{
    /**
     *  文章是草稿
     */
    public static final int ARTICLE_STATUS_DRAFT = 1;
    /**
     *  文章是正常分布状态
     */
    public static final int ARTICLE_STATUS_NORMAL = 0;

    /**
    要求分类状态为正常状态
     */
    public static final String STATUS_NORMAL ="0";

    /**
     * 友链审核通过（百度图标）
     */
    public static final String LINK_STATUS_NORMAL="0";

    //评论为友链的评论
    public static final String FRIEND_LIST_COMMENT = "1";

    //评论为根评论的友链评论
    public static final  String ROOT_COMMENT = "-1";

    //redis 评论总数的key值
    public static final  String  VIEW_COUNT = "viewCount";

    //权限的菜单
    public static final  String MENU = "c";

    //权限的按钮
    public static final  String BUTTON = "F";

    //区分后台用户还是前台用户
    public static final  String ADMAIN = "1";


}
