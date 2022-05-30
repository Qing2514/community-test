package com.yx.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.pagehelper.PageInfo;
import com.yx.model.*;
import com.yx.service.IHouseService;
import com.yx.service.IOwnerService;
import com.yx.service.IPropertyInfoService;
import com.yx.service.IPropertyTypeService;
import com.yx.util.JsonObject;
import com.yx.util.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


/**
 * 物业收费控制器
 */
@Api(tags = {""})
@RestController
@RequestMapping("/propertyinfo")
public class PropertyInfoController {

    private Logger log = LoggerFactory.getLogger(getClass());

    @Resource
    private IPropertyInfoService propertyInfoService;

    @Resource
    private IPropertyTypeService propertyTypeService;

    @Resource
    private IHouseService houseService;


    /**
     * 查询所有基本信息
     *
     * @param propertyInfo 过滤项
     * @param numbers
     * @param page
     * @param limit
     * @return
     */
    @RequestMapping("/queryPropertyAll")
    public JsonObject queryPropertyAll(PropertyInfo propertyInfo, String numbers,
                                       @RequestParam(defaultValue = "1") Integer page,
                                       @RequestParam(defaultValue = "15") Integer limit) {
        if (numbers != null) {
            if (numbers.length() > 50) return new JsonObject(400, "fail", null, null);
            House house = new House();
            house.setNumbers(numbers);
            propertyInfo.setHouse(house);
        }

        PageInfo<PropertyInfo> pageInfo = propertyInfoService.findPropertyInfoAll(page, limit, propertyInfo);
        return new JsonObject(0, "ok", pageInfo.getTotal(), pageInfo.getList());

    }

    /**
     * 添加物业需缴费记录
     *
     * @param propertyInfo 物业需缴费记录
     * @return 添加物业需缴费记录结果
     */
    @ApiOperation(value = "新增")
    @RequestMapping("/initData")
    public R initData(@RequestBody PropertyInfo propertyInfo) {
        if (propertyInfo.getTypeId() != null && propertyInfo.getTypeId() == 1)
            propertyInfo.setNumber(propertyInfo.getHouse().getArea());
        // 检查
        if (checkExist(propertyInfo)) {
            propertyInfoService.add(propertyInfo);
            return R.ok();
        } else return R.fail("fail");

    }


    /**
     * 根据ID删除多个物业缴费记录
     *
     * @param ids 多个缴费ID 以,分开
     * @return 删除结果
     */
    @ApiOperation(value = "删除")
    @RequestMapping("/deleteByIds")
    public R delete(String ids) {
        String reg = "((\\d+,)*)\\d";
        if (!ids.matches(reg)) return R.fail("fail");
        List<String> list = Arrays.asList(ids.split(","));
        for (String id : list) {
            if (propertyInfoService.findById(Long.valueOf(id)) == null)
                return R.fail("fail");
        }
        for (String id : list) {
            Long idLong = new Long(id);
            propertyInfoService.delete(idLong);
        }
        return R.ok();
    }

    /**
     * 根据ID更新物业缴费记录为已缴费
     *
     * @param id 缴费ID
     * @return 更新结果
     */
    @ApiOperation(value = "更新")
    @RequestMapping("/update")
    public R update(Integer id) {
        PropertyInfo propertyInfo = new PropertyInfo();
        propertyInfo.setId(id);
        propertyInfo.setStatus(1);
        int num = propertyInfoService.updateData(propertyInfo);
        if (num > 0) {
            return R.ok();
        }
        return R.fail("fail");
    }


    /**
     * 根据id查询缴费记录
     *
     * @param id 缴费ID
     * @return 缴费记录
     */
    @ApiOperation(value = "id查询")
    @GetMapping("{id}")
    public PropertyInfo findById(@PathVariable Long id) {
        return propertyInfoService.findById(id);
    }


    private boolean checkExist(PropertyInfo info) {
        PropertyType type = propertyTypeService.findById(new Long(info.getTypeId()));
        if (type != null)
            info.setMoney(type.getPrice() * info.getNumber());
        else return false;
        return houseService.findById((long) info.getHouseId()) != null;
    }


}
