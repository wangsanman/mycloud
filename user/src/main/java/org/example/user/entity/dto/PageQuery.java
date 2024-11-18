package org.example.user.entity.dto;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("分页查询参数")
public class PageQuery {
    @ApiModelProperty("页码")
    private long pageNo;
    @ApiModelProperty("每页条数")
    private long pageSize;
    @ApiModelProperty("排序字段")
    private String sortBy;
    @ApiModelProperty("是否升序")
    private Boolean isAsc = false;

    public <T> Page<T> toPage() {
        //封装分页参数
        Page<T> page = Page.of(pageNo, pageSize);
        //排序设置
        if (StrUtil.isNotBlank(sortBy)) {
            page.addOrder(new OrderItem().setAsc(isAsc).setColumn(sortBy));
        } else {
            page.addOrder(OrderItem.desc("update_time"));
        }
        return page;
    }

    /**
     * 默认排序灵活配置
     *
     * @param orderItem
     * @param <T>
     * @return
     */
    public <T> Page<T> toPageByOrderItemSort(OrderItem... orderItem) {
        //封装分页参数
        Page<T> page = Page.of(pageNo, pageSize);
        //排序设置
        if (StrUtil.isNotBlank(sortBy)) {
            page.addOrder(new OrderItem().setAsc(isAsc).setColumn(sortBy));
        } else {
            page.addOrder(orderItem);
        }
        return page;
    }
}
