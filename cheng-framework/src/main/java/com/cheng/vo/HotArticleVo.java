package com.cheng.vo;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HotArticleVo {

    @TableId
    private Long id;

    //标题
    private String title;

    //访问量
    private long viewCount;

}
