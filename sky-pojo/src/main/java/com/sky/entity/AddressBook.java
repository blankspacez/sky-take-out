package com.sky.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 地址簿
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "地址簿")
@TableName("address_book")
public class AddressBook implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "Id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    //用户id
    @ApiModelProperty(value = "用户id")
    private Long userId;

    //收货人
    @ApiModelProperty(value = "收货人")
    private String consignee;

    //手机号
    @ApiModelProperty(value = "手机号")
    private String phone;

    //性别 0 男 1 女
    @ApiModelProperty(value = "性别 1 女 0 男")
    private String sex;

    //省级区划编号
    @ApiModelProperty(value = "省级区划编号")
    private String provinceCode;

    //省级名称
    @ApiModelProperty(value = "省级名称")
    private String provinceName;

    //市级区划编号
    @ApiModelProperty(value = "市级区划编号")
    private String cityCode;

    //市级名称
    @ApiModelProperty(value = "市级名称")
    private String cityName;

    //区级区划编号
    @ApiModelProperty(value = "区级区划编号")
    private String districtCode;

    //区级名称
    @ApiModelProperty(value = "区级名称")
    private String districtName;

    //详细地址
    @ApiModelProperty(value = "详细地址")
    private String detail;

    //标签
    @ApiModelProperty(value = "标签")
    private String label;

    //是否默认 0否 1是
    @ApiModelProperty(value = "是否默认 0否 1是")
    private Integer isDefault;
}
