package ru.galuzin.jdbc.repository;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.galuzin.jdbc.entity.collections.Region;

import java.util.List;
import java.util.UUID;

@Repository
public interface RegionRepository extends CrudRepository<Region, UUID> {

    @Query("""
            SELECT r.*, az.id as availability_zone_id, az.region_id as availability_zone_region_id
                        FROM region r
                        LEFT JOIN availability_zone az ON r.id = az.region_id""")
    List<Region> takeWithZones();

    @Query("""
            SELECT r, az
                        FROM Region r
                        LEFT JOIN AvailabilityZone az ON r.id = az.region_id""")
    List<Region> take2WithZones();
}
