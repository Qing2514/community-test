package com.yx.controller;


import com.github.pagehelper.PageInfo;
import com.yx.model.PropertyInfo;
import com.yx.model.PropertyType;
import com.yx.model.RecordVo;
import com.yx.model.Records;
import com.yx.service.IPropertyInfoService;
import com.yx.service.IPropertyTypeService;
import com.yx.service.IRecordsService;
import com.yx.service.IUserinfoService;
import com.yx.service.impl.UserinfoServiceImpl;
import com.yx.util.IdWorker;
import com.yx.util.JsonObject;
import com.yx.util.R;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * 抄表控制器
 */
@RestController
@RequestMapping("/records")
public class RecordsController {

    private Logger log = LoggerFactory.getLogger(getClass());

    @Resource
    private IRecordsService recordsService;

    @Resource
    private IPropertyTypeService propertyTypeService;

    @Resource
    private IPropertyInfoService propertyInfoService;

    @Resource
    private IUserinfoService userinfoService;

    @Autowired
    private IdWorker idWorker;

    /**
     * 分页查询抄表记录信息
     *
     * @param page
     * @param limit
     * @param recordVo
     * @return
     */
    @RequestMapping("/queryRecordsAll")
    public JsonObject queryRecordsAll(@RequestParam(defaultValue = "1") Integer page,
                                      @RequestParam(defaultValue = "15") Integer limit,
                                      RecordVo recordVo) {
        PageInfo<RecordVo> pageInfo = recordsService.findRecordsAll(page, limit, recordVo);
        return new JsonObject(0, "ok", pageInfo.getTotal(), pageInfo.getList());

    }


    /**
     * 抄表的添加工作
     */
    @RequestMapping("/add")
    @Transactional(rollbackFor = {RuntimeException.class, Error.class})
    public R add(@RequestBody Records records) {
        /*
           步骤：
             1、添加抄表记录信息
                 1.1 根据记录中的房子和类型查询上次抄表的度数以及相关时间信息
                     如果存在，需要获取上次的度数
                     如果不存在 上次度数设置为0
                 1.2 添加抄表数据
             2、添加物业收费信息
                2.1  获取 上次到这次之间的度数信息
                2.2 根据收费类型查询收费标准
                2.3 用1的度数×2的的收费标准 获取相关费用
                2.4  并设定为未缴费状态
                2.5 如果存在上次抄表记录，添加物业收费信息
         */

        //根据参数房子id和类型id 获取最后一次登记信息
        Integer houId = records.getHouseId();
        Integer typeId = records.getTypeId();
        String meter = records.getMeter();
        if (!propertyInfoService.checkExist(houId, typeId)
                || userinfoService.findByName(meter) == null) return R.fail("fail");
        //获取最后一次记录信息
        Records rec = recordsService.queryByHouIdAndTypeId(houId, typeId);
        PropertyInfo info = new PropertyInfo();
        int num = -1;

        //2 如果存在，添加费用信息
        if (rec != null) {
            //根据类型的id查询类型的费用标准
            PropertyType type = propertyTypeService.findById((long) typeId);
            double price = type.getPrice();                         //获取收费标准
            double number = records.getNum2() - rec.getNum2();   //获取度数
            double money = number * price;

            info.setHouseId(houId);
            info.setTypeId(typeId);
            info.setStatus(0);//未缴费
            info.setNumber(number);
            info.setMoney(money);
            info.setRemarks(records.getRemarks());
            info.setStartDate(rec.getCheckTime());
            info.setEndDate(records.getCheckTime());
            propertyInfoService.add(info);                    //添加记录信息
            //获取上次表的度数  上次抄表时间
            records.setPropertyInfoId(info.getId());
            records.setNum(rec.getNum2());
        } else records.setNum(0.0);       // 同时修改本次抄表记录，记录上一次的度数

        //添加记录信息到数据库
        num = recordsService.add(records);

        if (num > 0) {
            return R.ok();
        } else {
            return R.fail("fail");
        }
    }

    /**
     * 根据ID删除抄表记录
     *
     * @param ids 需要删除的抄表记录ID
     * @return 删除结果
     */
    @RequestMapping("/deleteByIds")
    @Transactional(rollbackFor = {RuntimeException.class, Error.class})
    public R deleteByIds(String ids) {
        //把字符串转list集合
        List<String> list = Arrays.asList(ids.split(","));
        for (String id : list) {
            Long idLong = Long.parseLong(id);
            //根据id获取对应的记录信息获取登记时间以及房子id
            Records records = recordsService.findById(idLong);
            //删除登记表记录信息
            recordsService.delete(idLong);
            //删除物业收费信息表相关信息
            if (records.getPropertyInfoId() != null) propertyInfoService.delete((long) records.getPropertyInfoId());
        }
        return R.ok();
    }

}
