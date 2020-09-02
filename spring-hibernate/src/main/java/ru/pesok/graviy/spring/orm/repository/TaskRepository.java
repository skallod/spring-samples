//package ru.pesok.graviy.spring.orm.repository;
//
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//import ru.pesok.graviy.spring.orm.domain.TaskType;
//
//import javax.persistence.EntityManager;
//import javax.persistence.PersistenceContext;
//import java.util.List;
//
//@Service
//public class TaskRepository {
//
//    @PersistenceContext
//    private EntityManager em;
//
//    @Transactional
//    public void save(TaskType p) {
//        em.persist(p);
//    }
//
//    @Transactional(readOnly = true)
//    public List<TaskType> getAll() {
//        //jpql
//        return em.createQuery("select r from TaskType r", TaskType.class).getResultList();
//        //native
//        //return em.createNativeQuery("select * from bssi_task_type s", TaskType.class).getResultList();
//    }
//}
