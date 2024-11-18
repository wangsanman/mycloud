package org.example.user.service;

import org.example.user.entity.po.Address;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
class IAddressServiceTest {

    @Autowired
    private IAddressService addressService;

    @Test
    void save() {

        addressService.removeById(100);

        for (Address address : addressService.list()) {
            System.out.println(address.toString());
        }

    }

}