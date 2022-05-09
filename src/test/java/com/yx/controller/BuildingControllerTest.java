package com.yx.controller;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageInfo;
import com.yx.model.Building;
import com.yx.service.IBuildingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@SpringBootTest
class BuildingControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext wac;

    @MockBean
    private IBuildingService mockBuildingService;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    @Test
    void testQueryBuildAll() throws Exception {
        // Setup
        // Configure IBuildingService.findBuildAll(...).
        final Building building = new Building(0, "numbers", "remarks", "uints");
        final PageInfo<Building> buildingPageInfo = new PageInfo<>(Arrays.asList(building));
        when(mockBuildingService.findBuildAll(0, 0, "numbers")).thenReturn(buildingPageInfo);

        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(get("/building/queryBuildAll")
                .param("page", "0")
                .param("limit", "0")
                .param("numbers", "numbers")
                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        // 使用 JSON.toJSONString(building) 前需要将 model 文件夹中的实体类中的属性（除id外）按首字母进行排序
        assertThat(response.getContentAsString()).isEqualTo("{\"code\":0,\"msg\":\"ok\",\"count\":1," +
                "\"data\":[" + JSON.toJSONString(building) + "]}");
    }

}
