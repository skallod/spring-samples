package ru.galuzin.springrest.health;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.actuate.health.AbstractHealthIndicator;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.Status;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.Statement;

@Slf4j
public class CustomDbHealthIndicator extends AbstractHealthIndicator {

    protected final DataSource mainDataSource;

    protected final DataSource standInDataSource;

    public CustomDbHealthIndicator (
        DataSource mainDataSource,
        DataSource standInDataSource) {
        this.mainDataSource = mainDataSource;
        this.standInDataSource = standInDataSource;
    }

    protected void doHealthCheck(Health.Builder builder) throws Exception {
        builder.status(isAnyDataSourceAlive() ? Status.UP : Status.DOWN);
    }

    public boolean isAnyDataSourceAlive() {
        return checkDS(mainDataSource) || checkDS(standInDataSource);
    }

    private boolean checkDS(DataSource dataSource) {
        log.debug("Check ds start");
        boolean result = true;
        try (
            final Connection connection = dataSource.getConnection();
            final Statement statement = connection.createStatement();
        ) {
            statement.execute("SELECT 1");
        } catch (Exception e) {
            log.warn("Data source check service, exception message " + e.getMessage());
            result = false;
        }
        log.debug("Check ds finish");
        return result;
    }
}
