package com.yx.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yx.dao.HouseMapper;
import com.yx.dao.PropertyInfoMapper;
import com.yx.dao.PropertyTypeMapper;
import com.yx.model.PropertyInfo;
import com.yx.model.PropertyType;
import com.yx.service.IPropertyInfoService;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author kappy
 * @since 2020-11-08
 */
@Service
public class PropertyInfoServiceImpl extends ServiceImpl<PropertyInfoMapper, PropertyInfo> implements IPropertyInfoService {

    @Autowired
    private PropertyInfoMapper propertyInfoDao;

    @Autowired
    private PropertyTypeMapper propertyTypeMapper;

    @Autowired
    private HouseMapper houseMapper;

    @Override
    public PageInfo<PropertyInfo> findPropertyInfoAll(int page, int pagesise,
                                                      PropertyInfo propertyInfo) {
        PageHelper.startPage(page, pagesise);
        List<PropertyInfo> list = propertyInfoDao.queryListAll(propertyInfo);
        return new PageInfo(list);
    }


    @Override
    public IPage<PropertyInfo> findListByPage(Integer page, Integer pageCount) {
        IPage<PropertyInfo> wherePage = new Page<>(page, pageCount);
        PropertyInfo where = new PropertyInfo();

        return baseMapper.selectPage(wherePage, Wrappers.query(where));
    }

    @Override
    public int add(PropertyInfo propertyInfo) {
        return baseMapper.insert(propertyInfo);
    }

    @Override
    public int delete(Long id) {
        return baseMapper.deleteById(id);
    }

    @Override
    public int updateData(PropertyInfo propertyInfo) {
        return baseMapper.updateById(propertyInfo);
    }

    @Override
    public PropertyInfo findById(Long id) {
        return baseMapper.selectById(id);
    }

    @Override
    public boolean checkExist(Integer TypeId, Integer houseId) {
        return houseMapper.selectById((long) houseId) != null
                && propertyTypeMapper.selectById(TypeId) != null;
    }

    @Override
    public boolean checkExist(PropertyInfo info) {
        PropertyType type = propertyTypeMapper.selectById(info.getTypeId());
        if (type != null)
            info.setMoney(type.getPrice() * info.getNumber());
        else return false;
        return houseMapper.selectById((long) info.getHouseId()) != null
                && propertyTypeMapper.selectById(info.getTypeId()) != null;
    }
}
