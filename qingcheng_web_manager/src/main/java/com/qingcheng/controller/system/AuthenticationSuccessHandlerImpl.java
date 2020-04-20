package com.qingcheng.controller.system;

import com.alibaba.dubbo.config.annotation.Reference;
import com.qingcheng.pojo.system.LoginLog;
import com.qingcheng.service.system.LoginLogService;
import com.qingcheng.util.WebUtil;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

public class AuthenticationSuccessHandlerImpl implements AuthenticationSuccessHandler {

    @Reference
    LoginLogService loginLogService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest,
            HttpServletResponse httpServletResponse, Authentication authentication)
            throws IOException, ServletException {

        System.out.println("登陆成功了，我要在这里记录日志");
        String name = authentication.getName();
        String ip = httpServletRequest.getRemoteAddr();

        LoginLog loginLog = new LoginLog();
        loginLog.setIp(ip);
        loginLog.setLoginName(name);
        loginLog.setLoginTime(new Date());
        loginLog.setLocation(WebUtil.getCityByIP(ip));
        String header = httpServletRequest.getHeader("user-agent");
        loginLog.setBrowserName(WebUtil.getBrowserName(header));
        loginLogService.add(loginLog);


        httpServletRequest.getRequestDispatcher("/main.html")
                .forward(httpServletRequest,httpServletResponse);
    }
}
