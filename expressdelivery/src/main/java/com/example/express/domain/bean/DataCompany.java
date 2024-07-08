package com.example.express.domain.bean;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;

/**
 * 快递公司数据
 */
@Data
public class DataCompany implements Serializable {
    @TableId
    private Integer id;

    private String name;

    private String code;
}
