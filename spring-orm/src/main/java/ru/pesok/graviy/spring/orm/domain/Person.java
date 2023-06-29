package ru.pesok.graviy.spring.orm.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.Instant;

@Entity
public class Person {

    @Id
    //@GeneratedValue
    private int id;
    private String name;
    private String zone;
    @Column(name = "created_at")
    private Instant createdAt;

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

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public String getZone() {
        return zone;
    }

    public void setZone(String zone) {
        this.zone = zone;
    }

    @Override
    public String toString() {
        return "Person{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", zone='" + zone + '\'' +
            ", createdAt=" + createdAt +
            '}';
    }
}
