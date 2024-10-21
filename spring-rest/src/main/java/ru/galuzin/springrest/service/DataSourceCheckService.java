//package ru.galuzin.springrest.service;
//
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//
//import javax.sql.DataSource;
//import java.sql.Connection;
//import java.sql.Statement;
//
//@RequiredArgsConstructor
//@Slf4j
//public class DataSourceCheckService {
//    private final DataSource dataSource1;
//    private final DataSource dataSource2;
//
//    public boolean isAnyDataSourceActive() {
//        return checkDS(dataSource1) || checkDS(dataSource2);
//    }
//
//    private boolean checkDS(DataSource dataSource) {
//        boolean result = true;
//        try (
//            final Connection connection = dataSource.getConnection();
//            final Statement statement = connection.createStatement();
//        ) {
//            statement.execute("SELECT 1");
//        } catch (Exception e) {
//            log.warn("Data source check service, exception message " + e.getMessage());
//            result = false;
//        }
//        return result;
//    }
//}
