package com.yx.interceptor;

import com.yx.model.Userinfo;
import com.yx.util.CheckUtils;
import org.springframework.util.StreamUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class TypeInterceptor implements HandlerInterceptor {

    /**
     * 请求之前进行得调用
     *
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //获取请求参数
        String queryString = request.getQueryString();
        String uri = request.getRequestURI();
        boolean check = true;
        if (queryString != null && !StringUtils.isEmpty(queryString)
                && uri.contains("query") && uri.contains("All")) {
            String[] split = queryString.split("&");
            for (String str : split) {
                if (check && ( str.contains("page") || str.contains("limit"))) {
                    String value = str.split("=")[1];
                    check = !StringUtils.isEmpty(value) && CheckUtils.checkStringDigit(value);
                } else if (check && str.contains("number")) {
                    String value = str.split("=")[1];
                    check = CheckUtils.checkStringDigit(value) && value.length() <= 50;
                } else if (check && str.contains("status")) {
                    String value = str.split("=")[1];
                    check = CheckUtils.checkStringDigit(value) && (value.equals("1") || value.equals("0"));
                }
            }
        }
        //获取请求body
        byte[] bodyBytes = StreamUtils.copyToByteArray(request.getInputStream());
        String body = new String(bodyBytes, request.getCharacterEncoding());
        if (!check) response.sendRedirect(request.getContextPath() + "/fail");
        return check;
    }

    /**
     * 请求处理之后调用  但是再视频被渲染之前
     *
     * @param request
     * @param response
     * @param handler
     * @param modelAndView
     * @throws Exception
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    /**
     * 整个请求结束调用之后
     *
     * @param request
     * @param response
     * @param handler
     * @param ex
     * @throws Exception
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }

}
