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

    private static final String TOPIC_NAME = "test_galuzin";

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(ProducerMain.class, args);
//        {
//            KafkaTemplate<String, Book> template = (KafkaTemplate<String, Book>) context.getBean("template");
//            Book book = new Book();
//            book.setAuthor8("Vmnbmb");
//            Author author = new Author();
//            author.setName("petya");
//            book.setHuman(author);
//            template.send("gal-test-topic", "1", book);
//        }
        int i = 0;
        for (; i < 100 ; i++) {
            KafkaTemplate<String, String> template = (KafkaTemplate<String, String>) context.getBean("templateString");
            template.send(TOPIC_NAME, "1", "{\"author " + i +"\":\"from string\"}");
        }
    }

}
