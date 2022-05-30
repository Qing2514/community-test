package com.yx.interceptor;

import com.alibaba.fastjson.JSON;
import com.yx.util.CheckUtils;
import com.yx.util.HttpHelper;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

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
        System.out.println("请求uri ： " + uri);
        boolean check = true;
        if (queryString != null && !StringUtils.isEmpty(queryString)
                && uri.contains("query") && uri.contains("All")) {
            check = checkQueryAllParams(queryString, check);
        }
        //获取请求body
        if (uri.contains("initData")) {
            String body = HttpHelper.getBodyString(request);
            System.out.println("请求体 ： " + body);
            try {
                Object jsonBody = JSON.parse(body);    //先转换成Object
                Map map = (Map) jsonBody;
                System.out.println(map);
                check = checkInitData(map);
            }catch (Exception e){
                response.sendRedirect(request.getContextPath() + "/fail");
                return false;
            }
        }
        else if(uri.contains("add") && uri.contains("records")){
            String body = HttpHelper.getBodyString(request);
            System.out.println("请求体 ： " + body);
            try {
                Object jsonBody = JSON.parse(body);    //先转换成Object
                Map map = (Map) jsonBody;
                System.out.println(map);
                check = checkAdd(map);
            }catch (Exception e){
                response.sendRedirect(request.getContextPath() + "/fail");
                return false;
            }
        }
        if (!check) response.sendRedirect(request.getContextPath() + "/fail");
        return check;
    }

    private boolean checkAdd(Map map) {
        boolean check = true;
        check = map.containsKey("houseId") && map.containsKey("typeId")
                && map.containsKey("num2") && map.containsKey("checkTime")
                && map.containsKey("meter");

        if (check) {
            int houseId = (int) map.get("houseId");
            int typeId = (int) map.get("typeId");
            int num2 = (int) map.get("num2");
            String checkTime = String.valueOf(map.get("checkTime"));
            String meter = String.valueOf(map.get("meter"));
            String regex = "\\d{4}-\\d{2}-\\d{2}";
            check = num2 >= 0 && num2 < 10000.0 &&
                    checkTime.matches(regex);
        }
        return check;
    }

    private boolean checkQueryAllParams(String queryString, boolean check) {
        String[] split = queryString.split("&");
        for (String str : split) {
            if (check && (str.contains("page") || str.contains("limit"))) {
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
        return check;
    }

    private boolean checkInitData(Map map) throws ParseException {
        boolean check = true;
        check = map.containsKey("houseId") && map.containsKey("typeId")
                && map.containsKey("number") && map.containsKey("status")
                && map.containsKey("startDate") && map.containsKey("endDate");

        if (check) {
            int houseId = (int) map.get("houseId");
            int typeId = (int) map.get("typeId");
            int number = (int) map.get("number");
            int status = (int) map.get("status");
            String startDate = String.valueOf(map.get("startDate"));
            String endDate = String.valueOf(map.get("endDate"));
            String regex = "\\d{4}-\\d{2}-\\d{2}";
            check = number >= 0 && number < 10000.0 &&
                    startDate.matches(regex) && endDate.matches(regex)
                    && (status ==0 ||status==1)
                    && checkDate(startDate, endDate);
        }
        return check;
    }

    private boolean checkDate(String start, String end) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date endDate = sdf.parse(end);
        Date startDate = sdf.parse(start);
        long nd = 1000 * 24 * 60 * 60;
        long diff = endDate.getTime() - startDate.getTime();
        // 计算差多少天
        long day = diff / nd;
        return day <= 100 && day >= 90;
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
