package ru.pesok.graviy.spring.orm.domain;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Person {

    @Id
    //@GeneratedValue
    private int id;
    private String name;
    private String zone;

    public Person() {
    }

    public Person(int id, String name, String zone) {
        this.id = id;
        this.name = name;
        this.zone = zone;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Person{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", zone='" + zone + '\'' +
                '}';
    }
}
