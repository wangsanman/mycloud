package org.example.user.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 地址表
 * </p>
 *
 * @author man
 * @since 2024-11-09
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("address")
@ApiModel(value = "Address对象", description = "地址表")
public class Address implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键，自增 ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "用户 ID")
    private Long userId;

    @ApiModelProperty(value = "省份")
    private String province;

    @ApiModelProperty(value = "城市")
    private String city;

    @ApiModelProperty(value = "区/县")
    private String town;

    @ApiModelProperty(value = "手机号码")
    private String mobile;

    @ApiModelProperty(value = "街道地址")
    private String street;

    @ApiModelProperty(value = "联系人")
    private String contact;

    @ApiModelProperty(value = "是否为默认地址")
    private Boolean isDefault;

    @ApiModelProperty(value = "备注")
    private String notes;

    @ApiModelProperty(value = "逻辑删除标志")
    private Boolean deleted;

}
