package com.galuzin.autoconf;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "test-settings.services.listener")
public class TestProperties3 {

    Boolean restartAfterAuthException;

    Boolean cacheEnabled = true;

    Integer authIntervalRetry = 10;
}
