package com.cheng.service.impl;

import com.cheng.domain.ResponseResult;
import com.cheng.domain.entity.LoginUser;
import com.cheng.domain.entity.User;
import com.cheng.service.LoginService;
import com.cheng.utils.JwtUtil;
import com.cheng.utils.RedisCache;
import com.cheng.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Service
public class SystemLoginServiceImpl implements LoginService {

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
        redisCache.setCacheObject("login:"+id,loginUser);

        Map<String,String> map = new HashMap<>();
        map.put("token",jwt);

        return ResponseResult.okResult(map);
    }


    @Override
    public ResponseResult logout() {
        Long userId = SecurityUtils.getUserId();

        redisCache.deleteObject("login:"+userId);
        return ResponseResult.okResult();
    }
}
