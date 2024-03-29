package ru.galuzin.jdbc.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.galuzin.jdbc.entity.inherit.InstancePlain;

import java.util.UUID;

@Repository
public interface InstancePlainRepository   extends CrudRepository<InstancePlain, UUID> {
}
