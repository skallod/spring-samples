package com.galuzin.autoconf;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "test-settings.services.props")
public class TestProperties2 {
    String prop1;
}
