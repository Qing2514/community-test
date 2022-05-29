package com.yx.interceptor;

import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @summary 自定义 DispatcherServlet 来分派 XinHttpServletRequestWrapper
 */
public class XinDispatcherServlet extends DispatcherServlet {

    /**
     * 包装成我们自定义的request
     *
     * @param request
     * @param response
     * @throws Exception
     */
    @Override
    protected void doDispatch(HttpServletRequest request, HttpServletResponse response) throws Exception {
        super.doDispatch(new XinHttpServletRequestWrapper(request), response);
    }
}