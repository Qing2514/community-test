package com.yx.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;
import com.yx.model.PropertyInfo;
import com.yx.model.PropertyType;
import org.apache.ibatis.annotations.Param;

import java.util.Date;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author kappy
 * @since 2020-11-08
 */
public interface IPropertyInfoService extends IService<PropertyInfo> {


    PageInfo<PropertyInfo> findPropertyInfoAll(int page, int pagesise,
                                               PropertyInfo propertyInfo);

    /**
     * 查询分页数据
     *
     * @param page      页码
     * @param pageCount 每页条数
     * @return IPage<PropertyInfo>
     */
    IPage<PropertyInfo> findListByPage(Integer page, Integer pageCount);

    /**
     * 添加
     *
     * @param propertyInfo 
     * @return int
     */
    int add(PropertyInfo propertyInfo);

    /**
     * 删除
     *
     * @param id 主键
     * @return int
     */
    int delete(Long id);

    /**
     * 修改
     *
     * @param propertyInfo 
     * @return int
     */
    int updateData(PropertyInfo propertyInfo);

    /**
     * id查询数据
     *
     * @param id id
     * @return PropertyInfo
     */
    PropertyInfo findById(Long id);

    boolean checkExist(PropertyInfo info) ;

    boolean checkExist(Integer houId, Integer typeId);
}
