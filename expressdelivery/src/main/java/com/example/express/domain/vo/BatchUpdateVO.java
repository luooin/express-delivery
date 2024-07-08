package com.example.express.domain.vo;

import lombok.Data;

@Data
public class BatchUpdateVO {
    private String id;

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

    private String pickupMethodx;
}
