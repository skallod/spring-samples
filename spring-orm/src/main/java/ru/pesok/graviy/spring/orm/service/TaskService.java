package ru.pesok.graviy.spring.orm.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.pesok.graviy.spring.orm.domain.TaskType;
import ru.pesok.graviy.spring.orm.repository.TaskRepository;

import java.util.List;

@Service
public class TaskService {

    Logger logger = LoggerFactory.getLogger(TaskService.class);

    @Autowired
    TaskRepository repository;

    @Transactional
    public void save(TaskType p) {
        repository.save(p);
    }

    //    @Transactional//(readOnly = true)
//    @Scheduled(cron = "0 16 16 * * *")
    public List<TaskType> getAll() {
        logger.info("gal task service invoked");
        try {
            return repository.getAll();
        } catch (DuplicateKeyException e) {
            logger.warn("gal duplicate key");
        } catch (Throwable e) {
            logger.error("gal task service fail", e);
        }
        return null;
    }
}
