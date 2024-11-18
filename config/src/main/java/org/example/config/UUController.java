package org.example.config;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/uu")
public class UUController {
    //    @Value("${nacos.name}")
    private String name;

    @GetMapping("a")
    public String a() {
        System.out.println("===========");
        System.out.println(name);
        System.out.println("===========");
        return "a";
    }
}
