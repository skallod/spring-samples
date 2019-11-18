package ru.pesok.graviy.spring.orm.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.pesok.graviy.spring.orm.domain.TaskType;
import ru.pesok.graviy.spring.orm.repository.TaskRepository;

import java.util.List;

@Service
public class TaskService {

    @Autowired
    TaskRepository repository;

    @Transactional
    public void save(TaskType p){
        repository.save(p);
    }

    @Transactional(readOnly = true)
    public List<TaskType> getAll(){
        return repository.getAll();
    }
}
