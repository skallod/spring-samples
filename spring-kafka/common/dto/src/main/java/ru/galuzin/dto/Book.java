package ru.galuzin.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Book {

    @JsonProperty(required = true)
    String author8;

    @JsonProperty
    Human human;

    public Book() {

    }

    public Book(String author8) {
        this.author8 = author8;
    }

    public String getAuthor8() {
        return author8;
    }

    @JsonProperty(required = true)
    public void setAuthor8(String author) {
        this.author8 = author;
    }

    public Human getHuman() {
        return human;
    }

    public void setHuman(Human human) {
        this.human = human;
    }
}
