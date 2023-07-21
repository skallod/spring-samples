package ru.galuzin.jdbc.repository;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.galuzin.jdbc.entity.json.*;

import java.util.Optional;
import java.util.UUID;

public class ServiceRepositoryTest extends AbstractDaoTest {

    @Autowired
    ServiceRepository serviceRepository;

    @Autowired
    Service2Repository service2Repository;

    /**
     * снчала плейн затем full
     * тест в обратном порядке
     */
    @Test
    public void test1() {
        var service = Service.builder()
            .name("service1")
            .common(
                CommonInfo.<ServiceData>builder()
                    .entityData(
                        ServiceData.builder()
                            .dbUrl("dev:35432")
                            .count(7)
                            .transitSettings(
                                TransitSettings.builder()
                                    .maxTimeout(10000)
                                    .build()
                            )
                            .build()
                    )
                    .build()
            )
            .build();
        final Service saved = serviceRepository.save(service);
        System.out.println("saved = " + saved);
//        var service2 = Service2.builder()
//            .name("service2")
//            .common(
//                CommonInfo.<KafkaResource>builder()
//                    .entityData(
//                        KafkaResource.builder()
//                            .kafkaHost("st:909")
//                            .replicationFactor(3)
//                            .build()
//                    )
//                    .build()
//            )
//            .build();
//        final Service2 saved2 = service2Repository.save(service2);
//        System.out.println("saved2 = " + saved2);
    }

    @Test
    public void test2() {
        final Optional<Service2> byId = service2Repository.findById(UUID.fromString("2428e5fd-e2fe-4716-8664-d51f211fb037"));
        System.out.println("byId = " + byId);
    }
}
