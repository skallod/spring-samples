//package ru.galuzin.springrest.health;
//
//import org.springframework.boot.actuate.availability.LivenessStateHealthIndicator;
//import org.springframework.boot.availability.ApplicationAvailability;
//import org.springframework.boot.availability.AvailabilityState;
//import org.springframework.boot.availability.LivenessState;
//import org.springframework.stereotype.Component;
//import ru.galuzin.springrest.service.DataSourceCheckService;
//
//@Component
//public class CustomLivenessProbe extends LivenessStateHealthIndicator {
//
//  private final DataSourceCheckService dataSourceCheckService;
//
//  public CustomLivenessProbe(ApplicationAvailability availability,
//                             DataSourceCheckService dataSourceCheckService) {
//    super(availability);
//    this.dataSourceCheckService = dataSourceCheckService;
//  }
//
//  @Override
//  protected AvailabilityState getState(ApplicationAvailability applicationAvailability) {
//    return dataSourceCheckService.isAnyDataSourceActive()
//        ? LivenessState.CORRECT
//        : LivenessState.BROKEN;
//  }
//
//}
