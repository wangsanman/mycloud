package org.example.user.entity.dto;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@ApiModel("分页返回数据")
@Data
public class PageDto<T> {
    @ApiModelProperty("总条数")
    private long total;
    @ApiModelProperty("总页数")
    private long pages;
    @ApiModelProperty("集合")
    private List<T> list;

    public static <PO, VO> PageDto<VO> pageToDto(Page<PO> page, Class<VO> clazz) {

        //封装返回值
        PageDto<VO> pageDto = new PageDto<>();
        pageDto.setPages(page.getPages());
        pageDto.setTotal(page.getTotal());
        List<PO> users = page.getRecords();
        pageDto.setList(BeanUtil.copyToList(users, clazz));

        return pageDto;
    }

    public static <PO, VO> PageDto<VO> pageToDtoByLambda(Page<PO> page, Function<PO, VO> function) {

        //封装返回值
        PageDto<VO> pageDto = new PageDto<>();
        pageDto.setPages(page.getPages());
        pageDto.setTotal(page.getTotal());
        List<PO> records = page.getRecords();
        pageDto.setList(records.stream().map(function).collect(Collectors.toList()));

        return pageDto;
    }
}
