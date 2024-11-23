package org.example.rpc.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
@ApiModel("用户详细信息")
public class UserInfo {
    @ApiModelProperty("年龄")
    private Integer age;
    @ApiModelProperty("介绍")
    private String intro;
    @ApiModelProperty("性别")
    private String gender;
}
