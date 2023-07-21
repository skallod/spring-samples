package ru.galuzin.jdbc.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.galuzin.jdbc.entity.json.Service2;

import java.util.UUID;

@Repository
public interface Service2Repository extends CrudRepository<Service2, UUID> {
}
