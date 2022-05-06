package com.yx;

import com.yx.controller.BuildingController;
import com.yx.dao.BuildingMapper;
import com.yx.model.Building;
import com.yx.service.impl.BuildingServiceImpl;
import com.yx.util.SpringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class CommunityApplicationTests {

    @Autowired
    public BuildingController buildingController;

    @Autowired
    public BuildingServiceImpl buildingService;

    @Autowired
    public BuildingMapper buildingMapper;

    @Test
    void contextLoads() {
        BuildingServiceImpl service = SpringUtils.getBean(BuildingServiceImpl.class);
        Building byId = service.findById(1L);
        System.out.println("id  = 1 : " + byId);
        System.out.println(service);
    }

    @Test
    void controllerTest(){
        Building building = buildingController.findById(1L);
        assert building != null && building.getNumbers().equals("16栋");
    }

    @Test
    void serviceTest(){
        Building building = buildingService.findById(1L);
        assert building != null && building.getNumbers().equals("16栋");
    }

    @Test
    void mapperTest(){
        Building building = buildingMapper.selectById(1L);
        assert building != null && building.getNumbers().equals("16栋");
    }

}
