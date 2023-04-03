package ru.galuzin.jdbc.repository;

import org.springframework.data.repository.CrudRepository;
import ru.galuzin.jdbc.entity.Region;
import ru.galuzin.jdbc.entity.inherit.InstanceFull;

import java.util.UUID;

public interface InstanceFullRepository extends CrudRepository<InstanceFull, UUID> {
}
