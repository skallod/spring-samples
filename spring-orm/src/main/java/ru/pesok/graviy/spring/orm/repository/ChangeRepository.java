package ru.pesok.graviy.spring.orm.repository;

import org.postgresql.util.PGobject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.UUID;

@Repository
public class ChangeRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Transactional
    public void saveChange() {
        String SAVE_CHANGE_SQL = "insert into changes (id, built_at, inserted_at, owner, data) " +
                "values (?, ?, ?, ?, ?)";
        jdbcTemplate.update(SAVE_CHANGE_SQL, ps -> {
            final String changeAsJson = "{\"tdata\":\"tdata\"}";

            final PGobject pGobject = new PGobject();
            pGobject.setType("jsonb");
            pGobject.setValue(changeAsJson);
            Timestamp timestamp = new Timestamp(Instant.now().toEpochMilli());
            ps.setObject(1, UUID.randomUUID());
            ps.setTimestamp(2, timestamp);
            ps.setTimestamp(3, timestamp);
            ps.setString(4, "tdata");
            ps.setObject(5, pGobject);
        });
    }
}
