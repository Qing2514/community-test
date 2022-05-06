package com.yx;

import com.yx.model.Building;
import com.yx.service.impl.BuildingServiceImpl;
import com.yx.util.SpringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class CommunityApplicationTests {

    @Test
    void contextLoads() {
        BuildingServiceImpl service = SpringUtils.getBean(BuildingServiceImpl.class);
        Building byId = service.findById(1L);
        System.out.println("id  = 1 : " + byId);
        System.out.println(service);
    }

}
