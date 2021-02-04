package ru.pesok.graviy.spring.orm.repository;

import org.postgresql.util.PGobject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.pesok.graviy.spring.orm.domain.TaskType;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;

@Service
public class TaskRepository {

    private static final String SAVE_SQL = "insert into changes (id, built_at, inserted_at, owner, data) " +
            "values (?, ?, ?, ?, ?)";

    private static final Logger log = LoggerFactory.getLogger(TaskRepository.class);

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Transactional
    public void save(TaskType p) {
        em.persist(p);
    }

    //@Transactional//(readOnly = true)
    public List<TaskType> getAll() {
        //jpql
//        return em.createQuery("select r from TaskType r", TaskType.class).getResultList();
        //native
//        return em.createNativeQuery("select * from bssi_task_type s", TaskType.class).getResultList();
//        em.createNativeQuery("insert into changes(id, built_at, inserted_at, owner, data) values ('" + UUID.randomUUID().toString() + "', now(), now() , 'hakani     ', '{\"tdata\":\"tdata\"}');\n")
//        em.createNativeQuery("insert into changes(id, built_at, inserted_at, owner, data) values ('b8d05f28-b398-4ef3-8ea7-52d896cb3f83', now(), now() , 'hakani', '{\"tdata\":\"tdata\"}');\n")
//                .executeUpdate();

//            jdbcTemplate.update(SAVE_SQL, ps -> {
//                final PGobject pGobject = new PGobject();
//                pGobject.setType("jsonb");
//                pGobject.setValue("{\"tdata\":\"tdata\"}");
//
//                ps.setObject(1, UUID.fromString("b8d05f28-b398-4ef3-8ea7-52d896cb3f83"));
//                ps.setTimestamp(2, new Timestamp(Instant.now().toEpochMilli()));
//                ps.setTimestamp(3, new Timestamp(Instant.now().toEpochMilli()));
//                ps.setString(4, "hakani");
//                ps.setObject(5, pGobject);
//            });
        final CountDownLatch latch = new CountDownLatch(1);
        Runnable r = () -> {
            try {
                latch.await();
            } catch (InterruptedException e) {
                log.error("galuzin await error",e);
            }
            log.info("galuzin start query");
            try {
                jdbcTemplate.execute("select changes_partioning_all_owners()");
//                jdbcTemplate.query("select changes_partitioning_init(?)",
//                        (ps) -> {
//                            ps.setString(1, "hakani");
//                        },
//                        (rs) -> {
//                        }
//                );
            } catch (Exception e) {
                log.error("galuzin query error",e);
            }
        };
//        CompletableFuture.runAsync(r);
//        CompletableFuture.runAsync(r);
//        CompletableFuture.runAsync(r);
        latch.countDown();
        return null;
    }

    public void part(){
        jdbcTemplate.query("select changes_partitioning_init(?)",
            (ps) -> {
                ps.setString(1, "selfcheck");
                log.info("part init invoked");
            },
            (rs) -> {
            }
        );
    }
}
