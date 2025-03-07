package com.sky.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 购物车
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "ShoppingCart")
public class ShoppingCart implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "Id")
    private Long id;

    //名称
    @ApiModelProperty(value = "名称")
    private String name;

    //用户id
    @ApiModelProperty(value = "用户id")
    private Long userId;

    //菜品id
    @ApiModelProperty(value = "菜品id")
    private Long dishId;

    //套餐id
    @ApiModelProperty(value = "套餐id")
    private Long setmealId;

    //口味
    @ApiModelProperty(value = "口味")
    private String dishFlavor;

    //数量
    @ApiModelProperty(value = "数量")
    private Integer number;

    //金额
    @ApiModelProperty(value = "金额")
    private BigDecimal amount;

    //图片
    @ApiModelProperty(value = "图片")
    private String image;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;
}
