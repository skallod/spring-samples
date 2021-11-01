package com.galuzin.autoconf;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "test-settings.services")
public class TestProperties1 {
    String str1;
    String str2;
}
