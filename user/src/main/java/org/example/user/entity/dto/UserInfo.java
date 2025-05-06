package org.example.user.entity.dto;

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
    @ApiModelProperty("地址")
    private String address;
    @ApiModelProperty("兴趣")
    private String[] hobbies;
}
