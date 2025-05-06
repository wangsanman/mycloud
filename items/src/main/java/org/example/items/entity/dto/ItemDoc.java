package org.example.items.entity.dto;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 商品表
 * </p>
 *
 * @author man
 * @since 2024-12-12
 */
@Data
@ApiModel("索引库实体")
public class ItemDoc implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "商品id")
    private String id;

    @ApiModelProperty(value = "SKU名称")
    private String name;

    @ApiModelProperty(value = "价格（分）")
    private Integer price;

    @ApiModelProperty(value = "商品图片")
    private String image;

    @ApiModelProperty(value = "类目名称")
    private String category;

    @ApiModelProperty(value = "品牌名称")
    private String brand;

    @ApiModelProperty(value = "销量")
    private Integer sold;

    @ApiModelProperty(value = "评论数")
    private Integer commentCount;

    @ApiModelProperty(value = "是否是推广广告")
    private Boolean isAD;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;
}
