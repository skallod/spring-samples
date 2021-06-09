package ru.pesok.graviy.spring.orm.service;

import org.postgresql.util.PSQLException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import ru.pesok.graviy.spring.orm.repository.ChangeRepository;
import ru.pesok.graviy.spring.orm.repository.TaskRepository;

@Service
public class ChangeService {

    @Autowired
    private ChangeRepository changeRepository;

    @Autowired
    private TaskRepository taskRepository;

    public void saveChange() {
        try {
            changeRepository.saveChange();
        } catch (DataIntegrityViolationException die) {
            if (die.getCause() != null && die.getCause() instanceof PSQLException) {
                PSQLException pEx = (PSQLException) die.getCause();
                if ("23514".equals(pEx.getSQLState())) {//no partition
                    System.out.println("error no partition");
                    taskRepository.part();
                }
            }
            throw die;
        }
    }
}
