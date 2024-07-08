package com.example.express.domain.vo;

import com.baomidou.mybatisplus.annotation.*;
import com.example.express.domain.enums.OrderDeleteEnum;
import com.example.express.domain.enums.OrderStatusEnum;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class OrderInfoVo { @TableId(type = IdType.ID_WORKER_STR)
private String id;

    private String userId;
    /**
     * 快递单号
     */
    private String odd;

    /**
     * 收件人
     */
    private String recName;
    /**
     * 收件电话
     */
    private String recTel;
    /**
     * 收货地址
     */
    private String recAddress;
    /**
     * 快递寄达地址
     */
    private String address;
    /**
     * 备注
     */
    private String remark;


    @TableField("status")
    private OrderStatusEnum orderStatus;

    /**
     * 取件方式
     */
    private String pickupMethodx;



    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createDate;


}
