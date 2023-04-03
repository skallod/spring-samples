package ru.galuzin.jdbc.repository;

import org.springframework.data.repository.CrudRepository;
import ru.galuzin.jdbc.entity.Region;
import ru.galuzin.jdbc.entity.inherit.InstancePlain;

import java.util.UUID;

public interface InstancePlainRepository   extends CrudRepository<InstancePlain, UUID> {
}
