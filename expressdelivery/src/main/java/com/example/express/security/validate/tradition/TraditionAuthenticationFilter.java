package com.example.express.security.validate.tradition;

import com.example.express.common.constant.SecurityConstant;
import com.example.express.security.exception.DefaultAuthException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * 传统登录的鉴权过滤器
 */
public class TraditionAuthenticationFilter extends AbstractAuthenticationProcessingFilter {
    /**
     * 是否仅 POST 方式
     */
    private boolean postOnly = true;

    private String SPRING_SECURITY_FORM_USERNAME_KEY = "username";

    private String SPRING_SECURITY_FORM_PASSWORD_KEY = "password";

    public TraditionAuthenticationFilter(RememberMeServices rememberMeServices) {
        // 传统登录的请求
        super(new AntPathRequestMatcher(SecurityConstant.LOGIN_PROCESSING_URL_FORM, "POST"));

        // 手动注入rememberService，支持自动登录
        this.setRememberMeServices(rememberMeServices);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        if (postOnly && !"POST".equals(request.getMethod())) {
            throw new AuthenticationServiceException(
                    "Authentication method not supported: " + request.getMethod());
        }



        String username = request.getParameter(SPRING_SECURITY_FORM_USERNAME_KEY);
        String password = request.getParameter(SPRING_SECURITY_FORM_PASSWORD_KEY);

        TraditionAuthenticationToken authRequest = new TraditionAuthenticationToken(username, password);

        // Allow subclasses to set the "details" property
        setDetails(request, authRequest);

        return this.getAuthenticationManager().authenticate(authRequest);
    }


    private void setDetails(HttpServletRequest request, TraditionAuthenticationToken authRequest) {
        authRequest.setDetails(authenticationDetailsSource.buildDetails(request));
    }
}
