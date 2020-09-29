package ru.galuzin.producer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.kafka.core.KafkaTemplate;
import ru.galuzin.dto.Author;
import ru.galuzin.dto.Book;
import ru.galuzin.dto.Human;

@SpringBootApplication
public class ProducerMain {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(ProducerMain.class, args);
        KafkaTemplate<String, Book> template = (KafkaTemplate<String, Book>) context.getBean("template");
        Book book = new Book();
        book.setAuthor8(null);
        Author author = new Author();
        author.setName("petya");
        book.setHuman(author);
        template.send("gal-test-topic","1", book);
    }

}
