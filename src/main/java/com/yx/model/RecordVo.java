package com.yx.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RecordVo extends  Records{
    //户主
    private String username;
    //门牌号
    private String numbers;
    //费用类型
    private String typename;
}
