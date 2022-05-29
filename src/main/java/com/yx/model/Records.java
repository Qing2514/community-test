package com.yx.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class Records implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private Integer typeId;//费用类型Id
    private Double num;//上次度数
    private Double num2;//本次度数
    private Integer houseId;
    private Integer propertyInfoId;
    private String meter;//抄表员

    /*@JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd")
    private Date upTime;//上次抄表时间

    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd")
    private Date onTime;//本次抄表时间*/

    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd")
    private Date checkTime;//登记时间

    private String remarks;

}
