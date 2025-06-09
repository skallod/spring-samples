package ru.galuzin.spring.ioc.dao;

import org.springframework.stereotype.Component;
import ru.galuzin.spring.ioc.domain.Person;

import java.util.stream.Collectors;

//@Component//(value = "personSimple")
public class PersonDaoSimple implements PersonDao {

    public Person findByName(String name) {
//        var str1 = name.chars().mapToObj(i -> String.valueOf(i)).collect(Collectors.joining(","));
//        System.out.println("str1 = " + str1);
        return new Person(name, 18);
    }
}
