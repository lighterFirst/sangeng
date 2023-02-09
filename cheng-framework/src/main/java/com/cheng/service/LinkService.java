package com.cheng.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cheng.domain.ResponseResult;
import com.cheng.domain.dto.AddLinkDto;
import com.cheng.domain.entity.Link;

/**
 * 友链(Link)表服务接口
 *
 * @author cheng
 * @since 2022-12-12 11:41:58
 */
public interface LinkService extends IService<Link> {

    ResponseResult getAllLink();

    ResponseResult<Link> listLink(Long pageNum, Long pageSize, Link link);

    ResponseResult<Link> addLink(AddLinkDto addLinkDto);

    ResponseResult showLink(Long id);

    ResponseResult updateLink(Link link);

    ResponseResult deleteLink(Long id);
}

