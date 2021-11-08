~/projects/localgit
-Dspring.profiles.active=dev -Dspring.cloud.config.uri="http://localhost:8887"
Arguments: --spring.config.location=classpath:/bootstrap.yml,classpath:/application.yml
hoxton actuator/bus-refresh
2020 actuator/busrefresh

spring.cloud.config или через bootstrap или через system

https://soshace.com/spring-cloud-config-refresh-strategies/

Intresting logs :
Received remote refresh request.
Fetching config from server at : http://localhost:8887
Keys refreshed [logging.level.ru.galuzin]
Registering MessageChannel springCloudBusInput