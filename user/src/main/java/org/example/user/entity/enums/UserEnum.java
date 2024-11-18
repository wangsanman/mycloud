package org.example.user.entity.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

@Getter
public enum UserEnum {
    NORMAL(0, "正常"),
    FREEZE(1, "冻结");

    @EnumValue //决定存到库里的值
    @JsonValue //决定展示给前端的值
    private Integer value;
    private String desc;

    UserEnum(Integer value, String desc) {
        this.value = value;
        this.desc = desc;
    }
}
