package org.example.user.entity.dto;

import lombok.Data;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("mycloud")
@Data
public class UserProperties {
    private Integer maxLoginErrorTime;
}
