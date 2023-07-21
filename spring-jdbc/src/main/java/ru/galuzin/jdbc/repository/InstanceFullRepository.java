package ru.galuzin.jdbc.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.galuzin.jdbc.entity.inherit.InstanceFull;

import java.util.UUID;

@Repository
public interface InstanceFullRepository extends CrudRepository<InstanceFull, UUID> {
}
