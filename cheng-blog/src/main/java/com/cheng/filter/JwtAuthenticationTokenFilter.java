package com.cheng.filter;

import com.alibaba.fastjson.JSON;
import com.cheng.domain.ResponseResult;
import com.cheng.domain.entity.LoginUser;
import com.cheng.enums.AppHttpCodeEnum;
import com.cheng.utils.JwtUtil;
import com.cheng.utils.RedisCache;
import com.cheng.utils.WebUtils;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

/**
 * 该类实现了OncePerRequestFilter，如果有token，是在登录状态后验证token，没有token，重新登录。
 */

/**
 * OncePerRequestFilter是在一次外部请求中只过滤一次。对于服务器内部之间的forward等请求，不会再次执行过滤方
 */

@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    @Autowired
    private RedisCache redisCache;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        //获取请求头的token
        String token = request.getHeader("token");
        if(!StringUtils.hasText(token)){
            //没有token，此时是还没有登录，直接放行。去登录
            filterChain.doFilter(request,response);
            //这里return的原因是，filter还会过滤response(响应请求).
            return;
        }
        //解析并获取userid
        Claims claims = null;
        try {
             claims = JwtUtil.parseJWT(token);
        } catch (Exception e) {
            e.printStackTrace();
            //token超时，token非法。告诉前端，需要重新登录
            ResponseResult result = ResponseResult.errorResult(AppHttpCodeEnum.NEED_LOGIN);
            WebUtils.renderString(response, JSON.toJSONString(request));
            return;
        }

        String userId = claims.getSubject();
        //从redis中获取用户信息
        LoginUser loginUser = redisCache.getCacheObject("bloglogin:" + userId);

        //如果没有从reids中获取到用户，说明用户过期。
        if(Objects.isNull(loginUser)){
            ResponseResult result = ResponseResult.errorResult(AppHttpCodeEnum.NEED_LOGIN);
            WebUtils.renderString(response, JSON.toJSONString(result));
            return;
        }

        //将loginUser包装成UsernamePasswordAuthenticationToken（使用三个参数的，表示已认证），存入securityContextHolder。

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginUser,null,null);
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        filterChain.doFilter(request,response);
    }
}
