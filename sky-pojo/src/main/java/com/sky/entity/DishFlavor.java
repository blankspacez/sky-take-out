package com.sky.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 菜品口味
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "菜品口味")
public class DishFlavor implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("口味id")
    private Long id;

    @ApiModelProperty("菜品id")
    //菜品id
    private Long dishId;

    @ApiModelProperty("口味名称")
    //口味名称
    private String name;

    @ApiModelProperty("口味数据list")
    //口味数据list
    private String value;

}
