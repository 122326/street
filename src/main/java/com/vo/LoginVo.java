package com.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Author:kai
 * Date:2022/9/10 21:21
 */
@Data
@ApiModel(value = "登陆参数")
public class LoginVo {

    @ApiModelProperty(value = "用户名", dataType = "String",required = true)
    private String phone;

    @ApiModelProperty(value = "密码", dataType = "String",required = true)
    private String password;
}
