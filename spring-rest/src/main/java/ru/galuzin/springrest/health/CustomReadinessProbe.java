//package ru.galuzin.springrest.health;
//
//import org.springframework.boot.actuate.availability.ReadinessStateHealthIndicator;
//import org.springframework.boot.availability.ApplicationAvailability;
//import org.springframework.boot.availability.AvailabilityState;
//import org.springframework.boot.availability.ReadinessState;
//import org.springframework.stereotype.Component;
//import ru.galuzin.springrest.service.DataSourceCheckService;
//
//@Component
//public class CustomReadinessProbe extends ReadinessStateHealthIndicator {
//
//    private final DataSourceCheckService dataSourceCheckService;
//
//    private volatile boolean ready;
//
//    public CustomReadinessProbe(ApplicationAvailability availability, DataSourceCheckService dataSourceCheckService) {
//        super(availability);
//        this.dataSourceCheckService = dataSourceCheckService;
//    }
//
//    @Override
//    protected AvailabilityState getState(ApplicationAvailability applicationAvailability) {
//        if (ready) {
//            return ReadinessState.ACCEPTING_TRAFFIC;
//        }
//        final boolean isAnyDataSourceActive = dataSourceCheckService.isAnyDataSourceActive();
//        if (isAnyDataSourceActive) {
//            ready = true;
//        }
//        return isAnyDataSourceActive
//            ? ReadinessState.ACCEPTING_TRAFFIC
//            : ReadinessState.REFUSING_TRAFFIC;
//    }
//
//}
