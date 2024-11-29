package org.example.user.controller;


import io.swagger.annotations.ApiOperation;
import org.example.common.IgnoreToken;
import org.example.user.entity.po.Address;
import org.example.user.service.IAddressService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * <p>
 * 地址表 前端控制器
 * </p>
 *
 * @author man
 * @since 2024-11-09
 */
@RestController
@RequestMapping("/address")
public class AddressController {
    @Resource
    private IAddressService addressService;


    @PostMapping
    @ApiOperation("添加地址")
    @IgnoreToken
    public void addAddress(@RequestBody Address address) {
        addressService.save(address);
    }

}
