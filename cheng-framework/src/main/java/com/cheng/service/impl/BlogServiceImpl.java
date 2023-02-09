package com.cheng.service.impl;

import com.cheng.domain.ResponseResult;
import com.cheng.domain.entity.LoginUser;
import com.cheng.domain.entity.User;
import com.cheng.service.BlogService;
import com.cheng.utils.BeanCopyUtils;
import com.cheng.utils.JwtUtil;
import com.cheng.utils.RedisCache;
import com.cheng.vo.BlogUserLoginVo;
import com.cheng.vo.UserInfoVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class BlogServiceImpl implements BlogService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private RedisCache redisCache;

    @Override
    public ResponseResult loginUser(User user) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user.getUserName(),user.getPassword());
        Authentication authentication = authenticationManager.authenticate(authenticationToken);

        if(Objects.isNull(authentication)){
            throw new RuntimeException("用户名或密码错误");
        }

        //获取userid,生成token
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        String id = loginUser.getUser().getId().toString();

        //使用jwt封装成token
        String jwt = JwtUtil.createJWT(id);

        //把用户信息存入redis
        redisCache.setCacheObject("bloglogin:"+id,loginUser);

        //把token和userinfo(登录实体类对象)封装  返回
        UserInfoVo userInfoVo = BeanCopyUtils.copyBean(loginUser.getUser(), UserInfoVo.class);
        BlogUserLoginVo blogUserLoginVo = new BlogUserLoginVo(jwt,userInfoVo);

        return ResponseResult.okResult(blogUserLoginVo);
    }

    @Override
    public ResponseResult logout() {

        //获取并解析token
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser = (LoginUser)authentication.getPrincipal();
        User user = loginUser.getUser();

        //删除redis存的用户信息
        redisCache.deleteObject("bloglogin:"+user.getId());

        return ResponseResult.okResult();
    }
}












