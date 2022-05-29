package com.yx.controller;

import com.yx.util.R;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 异常错误处理控制器
 */
@RestController
@RequestMapping("/fail")
public class ErrorController {

    @ApiOperation(value = "失败")
    @RequestMapping()
    public R fail() {
        return R.fail("fail");
    }
}
