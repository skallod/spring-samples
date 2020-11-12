package ru.galuzin.consumer.conf;

import org.apache.commons.lang3.StringUtils;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.common.config.SslConfigs;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
@ConfigurationProperties(prefix = "my.producer.security")
public class SslConfigProperties {

    private String protocol;
    private String trustStoreLocation;
    private String trustStorePassword;
    private String keyStoreLocation;
    private String keyStorePassword;
    private String keyPassword;
    private boolean enable;

    public Map<String, String> toMap() {
        Map<String, String> configMap = new HashMap<>();

        if (StringUtils.isNotEmpty(protocol)) {
            configMap.put(AdminClientConfig.SECURITY_PROTOCOL_CONFIG, protocol);
        }

        if (StringUtils.isNotEmpty(trustStoreLocation)) {
            configMap.put(SslConfigs.SSL_TRUSTSTORE_LOCATION_CONFIG, trustStoreLocation);
        }

        if (StringUtils.isNotEmpty(trustStorePassword)) {
            configMap.put(SslConfigs.SSL_TRUSTSTORE_PASSWORD_CONFIG, trustStorePassword);
        }

        if (StringUtils.isNotEmpty(keyStoreLocation)) {
            configMap.put(SslConfigs.SSL_KEYSTORE_LOCATION_CONFIG, keyStoreLocation);
        }

        if (StringUtils.isNotEmpty(keyStorePassword)) {
            configMap.put(SslConfigs.SSL_KEY_PASSWORD_CONFIG, keyStorePassword);
        }

        if (StringUtils.isNotEmpty(keyPassword)) {
            configMap.put(SslConfigs.SSL_KEYSTORE_PASSWORD_CONFIG, keyPassword);
        }
        return configMap;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getTrustStoreLocation() {
        return trustStoreLocation;
    }

    public void setTrustStoreLocation(String trustStoreLocation) {
        this.trustStoreLocation = trustStoreLocation;
    }

    public String getTrustStorePassword() {
        return trustStorePassword;
    }

    public void setTrustStorePassword(String trustStorePassword) {
        this.trustStorePassword = trustStorePassword;
    }

    public String getKeyStoreLocation() {
        return keyStoreLocation;
    }

    public void setKeyStoreLocation(String keyStoreLocation) {
        this.keyStoreLocation = keyStoreLocation;
    }

    public String getKeyStorePassword() {
        return keyStorePassword;
    }

    public void setKeyStorePassword(String keyStorePassword) {
        this.keyStorePassword = keyStorePassword;
    }

    public String getKeyPassword() {
        return keyPassword;
    }

    public void setKeyPassword(String keyPassword) {
        this.keyPassword = keyPassword;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }
}
