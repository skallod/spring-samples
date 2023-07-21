package ru.galuzin.jdbc.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.galuzin.jdbc.entity.collections.AvailabilityZone;

import java.util.UUID;

@Repository
public interface AvailabilityZoneRepository extends CrudRepository<AvailabilityZone, UUID> {
}
